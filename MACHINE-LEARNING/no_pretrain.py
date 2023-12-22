import pandas as pd
import os
import tensorflow as tf
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense
from keras.optimizers import Adam
from keras.preprocessing.image import ImageDataGenerator

BASE_DIR = "data/dog_disease"
BATCH_SIZE = 32
IMG_SIZE = (150, 150)
EPOCHS = 10

class MyCustomCallback(tf.keras.callbacks.Callback):
    def on_epoch_end(self, epoch, logs={}):
        if logs.get('loss') < 0.:
            print()
            self.model.stop_training = True

class MODEL_ONE():
    def preprocessing(BASE_DIR, IMG_SIZE, BATCH_SIZE):
        Datagen = ImageDataGenerator(
            rescale=1/255.0,
            shear_range=0.3,
            horizontal_flip=True,
            validation_split=0.3,
            brightness_range=[0.5, 1.5]
        )

        Train_Generator = Datagen.flow_from_directory(
            BASE_DIR,
            batch_size=BATCH_SIZE,
            target_size=IMG_SIZE,
            subset="training",
            class_mode="binary"
        )
        Validation_Generator = Datagen.flow_from_directory(
            BASE_DIR,
            batch_size=BATCH_SIZE,
            target_size=IMG_SIZE,
            subset="validation",
            class_mode="binary"
        )
        return Train_Generator, Validation_Generator

    def create_model(self):
        modeling = tf.keras.models.Sequential([
            Conv2D(32, (3, 3), 1, activation='relu', padding='same', input_shape=(224, 224, 3)),
            MaxPooling2D((2, 2), strides=(2, 2)),
            Conv2D(64, (3, 3), 1, activation='relu', padding='same'),
            MaxPooling2D((2, 2), strides=(2, 2)),
            Flatten(),
            Dense(128, activation='relu'),
            Dense(1, activation='sigmoid')
        ])

        modeling.compile(optimizer=Adam(), loss='binary_crossentropy', metrics=['accuracy'])
        modeling.summary()
        return modeling

    def train_model(model, Train_Generator, Validation_Generator, EPOCHS):
        model.fit(Train_Generator, epochs=EPOCHS, validation_data=Validation_Generator)
        return model

BATCH_SIZE = 32
IMG_SIZE = (150, 150)
EPOCHS = 10
class MODEL_TWO():
    def preprocessing(BASE_DIR, IMG_SIZE, BATCH_SIZE):
        Datagen = ImageDataGenerator(
            rescale=1/255.0,
            shear_range=0.3,
            horizontal_flip=True,
            validation_split=0.3,
            brightness_range=[0.5, 1.5]
        )
        Train_Generator = Datagen.flow_from_directory(
            BASE_DIR,
            batch_size=BATCH_SIZE,
            target_size=IMG_SIZE,
            subset="training",
            class_mode="binary"
        )
        Validation_Generator = Datagen.flow_from_directory(
            BASE_DIR,
            batch_size=BATCH_SIZE,
            target_size=IMG_SIZE,
            subset="validation",
            class_mode="binary"
        )
        return Train_Generator, Validation_Generator

    def create_model(self):
        modeling = tf.keras.models.Sequential([
            Conv2D(64, (3, 3), 1, activation='relu', padding='same', input_shape=(150, 150, 3)),
            Conv2D(64, (3, 3), 1, activation='relu', padding='same'),
            MaxPooling2D((2, 2), strides=(2, 2)),
            Conv2D(128, (3, 3), 1, activation='relu', padding='same'),
            Conv2D(128, (3, 3), 1, activation='relu', padding='same'),
            Flatten(),
            Dense(256, activation='relu'),
            Dense(1, activation='sigmoid')
        ])

        modeling.compile(optimizer=Adam(), loss='binary_crossentropy', metrics=['accuracy'])
        modeling.summary()
        return modeling

    def train_model(model, Train_Generator, Validation_Generator, EPOCHS):
        model.fit(Train_Generator, epochs=EPOCHS, validation_data=Validation_Generator)
        return model


if __name__ == '__main__':
    Train_Generator, validation_generator = MODEL_TWO.preprocessing(BASE_DIR, IMG_SIZE, BATCH_SIZE)
    model_two_instance = MODEL_TWO()
    modeling = model_two_instance.create_model()
    model = MODEL_TWO.train_model(modeling, Train_Generator, validation_generator, EPOCHS)
    model.save("MODEL_TWO_v5.h5")


