#include "rpcWiFi.h"
#include <Seeed_FS.h>
#include <Seeed_Arduino_FreeRTOS.h>
#include <PubSubClient.h>

const char* ssid = ""; // Replace this with WiFi network name
const char* password = ""; // Replace this with  WiFi network password

const char* mqttServer = "broker.hivemq.com"; // MQTT broker address
const int mqttPort = 1883; // MQTT port
const char* mqttTopic = "Intruder Detected"; // MQTT topic to publish

class BrokerPub { // declaring class to connect to broker and publish topic
private:
  const int PIR_PINNO = 2; //  pin used for PIR, should be modified if different pin used

  WiFiClient wifi;
  PubSubClient mqtt;

public:
  void startBroker() {
    Serial.begin(115200);
    while (!Serial); 
    mqtt.setClient(wifi);
    mqtt.setServer(mqttServer, mqttPort);
  }

  void loopBroker() {
    if (!mqtt.connected()) {
      reconnectBroker();
    }

    mqtt.loop();

    int motion = digitalRead(PIR_PINNO);
    if (motion == HIGH) {
      publish();
      
    }

    delay(100);
  }

private:
  void reconnectBroker() {
    while (!mqtt.connected()) {
      Serial.println("Connecting Wio to the hivemq broker");

      if (mqtt.connect("Wio_Terminal_Client")) {
        Serial.println("Wio connected to the broker");
      } else {
        Serial.print("Connecting to the broker failed, retrying in 5 seconds.....");
        delay(5000);
      }
    }
  }

  void publish() {
    Serial.println("Publishing topic");

    if (mqtt.publish(mqttTopic, "Intruder Detected")) {
      Serial.println("Topic published");
    } else {
      Serial.println("Topic publishing failed");
    }
  }
};

BrokerPub brokerPub; //class instantiation

void setup() {
  brokerPub.startBroker();
}

void loop() {
  brokerPub.loopBroker();
}

