import numpy as np
import tensorflow as tf
from tensorflow.keras.models import Model, Sequential, load_model, save_model
from tensorflow.keras.regularizers import l2
from tensorflow.keras.layers import Flatten, Dense

from model import get_model

from tfltransfer import bases
from tfltransfer import heads
from tfltransfer import optimizers
from tfltransfer.tflite_transfer_converter import TFLiteTransferConverter

if __name__ == "__main__":
    # Load the pre-process data. 
    x = np.load("x.npy") # x should be of shape (number of instances, window size, number of axes)
    y = np.load("y.npy") # y is a one-hot encoded representation of class labels.

    epochs = 15
    batch_size = 32
    tflite_model = "par_model"
    tflite_ondevice_model = "par_ondevice"
    encoder_layer = "encoder"

    window_size = x.shape[1]
    num_channels = x.shape[2]
    x_reshaped = x.reshape(-1, window_size * num_channels) 

    model = get_model()
    model.fit(x_reshaped, y, epochs = epochs, 
            batch_size = batch_size, verbose = 2)

    model = Model(model.input, model.get_layer(encoder_layer).output)
    save_model(model, tflite_model, 
                include_optimizer = False, 
                save_format="tf")
    

    # --------------- on-device model conversion ---------------- #
    
    # on-device model configuration.
    num_classes = 2
    learning_rate = 0.001
    batch_size = 5
    l2_rate = 0.0001
    hidden_units = 128
    input_shape = model.get_layer(encoder_layer).output.shape

    base = bases.SavedModelBase(tflite_model)

    head = Sequential([
        Flatten(input_shape=input_shape),
        Dense(units=hidden_units,
            activation="relu",
            kernel_regularizer=l2(l2_rate)),
        Dense(units=num_classes, 
            activation="softmax",
            kernel_regularizer=l2(l2_rate)),
    ])

    # Optimizer is ignored by the converter!
    head.compile(loss="categorical_crossentropy", optimizer="adam")

    converter = TFLiteTransferConverter(num_classes, 
                base,
                heads.KerasModelHead(head),
                optimizers.SGD(learning_rate),
                train_batch_size=batch_size) 
    
    converter.convert_and_save(tflite_ondevice_model)