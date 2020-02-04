from tensorflow.keras.models import Model
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.regularizers import l2
from tensorflow.keras.layers import Input, Conv2D, Flatten, Dense, MaxPool2D, Reshape

def get_model(input_shape = (2400,), num_classes = 6, learning_rate = 1e-4, l2_rate = 1e-4):

    input = Input(shape=input_shape)
    reshape_input = Reshape((1, 400, 6))(input)

    x = Conv2D(32, kernel_size = (1, 24), 
            strides = (1, 1),  
            activation = "relu", 
            padding = "valid", 
            kernel_regularizer = l2(l2_rate))(reshape_input)
    x = MaxPool2D((1, 4), (1, 2))(x)

    x = Conv2D(64, kernel_size = (1, 16), 
            strides = (1, 1), 
            activation = "relu", 
            padding = "valid", 
            kernel_regularizer = l2(l2_rate))(x)
    x = MaxPool2D((1, 4), (1, 2))(x)

    x = Conv2D(96, kernel_size = (1, 8), 
            strides = (1, 1), 
            activation = "relu", 
            padding = "valid", 
            kernel_regularizer = l2(l2_rate))(x)
    x = MaxPool2D((1, 4), (1, 2))(x)

    x = Conv2D(128, kernel_size = (1, 4), 
            strides = (1, 1), 
            activation = "relu", 
            padding = "valid", 
            kernel_regularizer = l2(l2_rate),
            name="encoder")(x)        
    x = Flatten()(x)
    output = Dense(num_classes, activation = "softmax")(x)

    model = Model(input, output)
    model.compile(optimizer = Adam(learning_rate), 
                loss = "categorical_crossentropy",
                metrics = ["categorical_accuracy"])

    return model