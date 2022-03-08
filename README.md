Introduction
------------

Hello, we are CSI7163 group 8: Jiacheng Hou (300125708) and Kaiyi Zhang (300070775). 

This project applies two deep learning models on Human Activity Recognition (HAR) dataset [1][2], which is available in the UCI Machine Learning Repository. 
The two deep learning models are Graph neural network (GNN) and Long Short-term Memory (LSTM). We also convert the GNN and LSTM models to .tflite and deploy them on Android. Our application can predict a person's activities in real-time, including  WALKING, WALKING_UPSTAIRS, WALKING_DOWNSTAIRS, SITTING, STANDING, LAYING. 
<br />
<br />

Contributions
------------
Jiacheng:
* Plot Data Visualisation
* Train a GNN model
* Deploy the GNN model on Android

Kaiyi:
* Train an LSTM model
* Deploy the LSTM model on Android
* Android application UI
<br />
<br />

Required Packages
------------
    implementation 'androidx.appcompat:appcompat:1.3.0'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    implementation 'org.tensorflow:tensorflow-lite:+'

<br />
<br />

References
------------
* [1] HAR Dataset. UCI Machine Learning Repository: Human Activity Recognition using smartphones data set. (2012). Retrieved March 8, 2022, from https://archive.ics.uci.edu/ml/datasets/human+activity+recognition+using+smartphones 
* [2] Roobini, M. S., & Naomi, M. J. F. (2019). Smartphone sensor-based human activity recognition using deep learning models. Int. J. Recent Technol. Eng, 8(1), 2740-2748.
