# On-device Activity Recognition
#### Personalized machine learning on the smartphone

The aim of the project is to provide an end-to-end solution for on-device training, inference and data collection for activity recongition based on <a href="https://github.com/tensorflow/examples/tree/master/lite/examples/model_personalization">TFlite Transfer Learning Pipeline</a>. The corresponding blog post is available <a href="https://aqibsaeed.github.io/on-device-activity-recognition">here</a>. 

#### Tools Required
* Python 3.5+
* Tensorflow 2.0.0rc0
* Numpy
* Pillow 
* Scipy
* Android Studio

#### Contribute (Future Work)
* Add support for pairing the app with a smartwatch and fine-tuning the model for a wearable device.
* Port SoundNet, add functionality for audio recording and tflite model conversion for handling dynamic size input.

If you are interested in contributing to this project, please submit a pull request or reach out at: aqibsaeed@protonmail.com.

#### Dataset 
The <a href="https://archive.ics.uci.edu/ml/datasets/Heterogeneity+Activity+Recognition">Heterogeneity Activity Recognition dataset</a> is used for model pretraining. If you use this in your research, please cite their work and check the license. 

#### Citing
If you find this project usefuly, please cite it as:

<pre>@misc{saeed2020recognition, 
  author = {Saeed, Aaqib},
  title = {On-device Activity Recognition},
  year = {2020},
  journal = {aqibsaeed.github.io},
  url = {\url{https://gitHub.com/aqibsaeed/on-device-activity-recognition}}
}</pre>
