
//Include the library
#include <LiquidCrystal_I2C.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include "RTClib.h"

// SENSOR JARAK
#define triggerPin  D8
#define echoPin     D7
#define led 2
#define STASSID "P24S"
#define STAPSK  "fayani123"

LiquidCrystal_I2C lcd(0x27, 16, 2);
RTC_DS1307 rtc;

const int pin_interrupt = 14; 
long int jumlah_tip = 0;
float curah_hujan = 0.00;
float curah_hujan_per_menit = 0.00;
float curah_hujan_per_jam = 0.00;
float curah_hujan_per_hari = 0.00;
float curah_hujan_hari_ini = 0.00;
float temp_curah_hujan_per_menit = 0.00;
float temp_curah_hujan_per_jam = 0.00;
float temp_curah_hujan_per_hari = 0.00;
float milimeter_per_tip = 0.47;
volatile boolean flag = false;

// SENSOR JARAK
long duration;
float jarak;
float latitude = 3.60458;
float longitude = 98.6248;

//waktu
String tanggal,jam,menit,detik,prediksi,kenaikan;

int cek = 0;
int x;
WiFiClient client;
HTTPClient http;

void ICACHE_RAM_ATTR hitung_curah_hujan()
{
  flag = true;
}
void initWiFi() { 
  WiFi.mode(WIFI_STA);
  WiFi.begin(STASSID, STAPSK);
  Serial.print("Connecting to WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    cek=cek+1;  
    if (cek==50) {
       ESP.restart();
 //       break;
    }
    lcd.clear(); 
    lcd.backlight();    
   // Pindahkan kursor ke kolom 0 dan baris 0
  // (baris 1)
    lcd.setCursor(0, 0);
    lcd.print("ONLINE & MONITOR");
    lcd.setCursor(0, 1);      
    lcd.print("LOADING...");
    lcd.setCursor(10, 1);
    lcd.print(cek);
    Serial.println(cek);
    delay(500);
  }
  //Serial.println(WiFi.localIP());
  //The ESP8266 tries to reconnect automatically when the connection is lost
  WiFi.setAutoReconnect(true);
  WiFi.persistent(true);
}

void setup() {
  //Init the serial port communication - to debug the library
  Serial.begin(115200); //Init serial port
  lcd.begin();
  //lcd.init();
  pinMode(pin_interrupt, INPUT);
  attachInterrupt(digitalPinToInterrupt(pin_interrupt), hitung_curah_hujan, FALLING);
  //SETUP SENSOR JARAK
  pinMode(triggerPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(led,OUTPUT);
  rtc.begin();
  if (!rtc.begin())
  {
    Serial.println("Couldn't find RTC");
    while (1);
  }
  if (!rtc.isrunning()){
    Serial.println("RTC lost power, let set time");
    rtc.adjust(DateTime(F(__DATE__), F(__TIME__)));
  }
  initWiFi();
  lcd.clear();
  lcd.setCursor(1,0);
  lcd.print("Ambil data...");
}

void loop() {
  if (flag == true) {
    curah_hujan += milimeter_per_tip;
    jumlah_tip++;
    delay(500);
    flag = false; 
  }
  bacaRTC();
  Serial.println(jam + ":" + menit + ":" + detik );
  lcd.setCursor(14, 0);
  lcd.print(detik);

  curah_hujan_hari_ini = jumlah_tip * milimeter_per_tip;
  temp_curah_hujan_per_menit = curah_hujan;
  if (detik.equals("19") || detik.equals("39")){
    curah_hujan_per_menit = temp_curah_hujan_per_menit; 
    }if (detik.equals("00")) {
    curah_hujan_per_menit = temp_curah_hujan_per_menit; 
    temp_curah_hujan_per_jam += curah_hujan_per_menit;  
    if (menit.equals("00")){
      curah_hujan_per_jam = temp_curah_hujan_per_jam;   
      temp_curah_hujan_per_hari += curah_hujan_per_jam; 
      temp_curah_hujan_per_jam = 0.00;                 
    }
    if (menit.equals("00") && jam.equals("00")){
      curah_hujan_per_hari = temp_curah_hujan_per_hari; 
      temp_curah_hujan_per_hari = 0.00;                 
      curah_hujan_hari_ini = 0.00;                      
      jumlah_tip = 0;                                   
    }
    temp_curah_hujan_per_menit = 0.00;
    curah_hujan = 0.00;
    delay(1000);
  }
  digitalWrite(led,LOW);
  digitalWrite(triggerPin, LOW);
  delayMicroseconds(2); 
  digitalWrite(triggerPin, HIGH);
  delayMicroseconds(10); 
  digitalWrite(triggerPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  jarak = ((duration/2) / 29.1);
  Serial.println(jarak);

  if (detik.equals("00") || detik.equals("01") || detik.equals("30") || detik.equals("31")){
    if ((WiFi.status() == WL_CONNECTED)) {
    lcd.clear();
    lcd.setCursor(1,0);
    lcd.print("Ambil data...");
    Serial.println(tanggal + " " + jam + ":" + menit + ":" + detik );
    Serial.println("jarak :"  + String(jarak) + "cm");
    String address;
      address ="http://ummuhafidzah.sch.id/kehadiran/aquawatch/update.php?tinggi=";
      //address = "http://192.168.86.78/sensorjarak/update.php?tinggi=";
      address += String(jarak);
      address += "&latitude="; 
      address += String(latitude);
      address += "&longitude="; 
      address += String(longitude);
      address += "&tanggal=";
      address += tanggal;
      address += "&jam=";
      address += jam;
      address += "&menit=";
      address += menit;
      address += "&detik=";
      address += detik;
      address += "&curah_menit=";
      address += String(curah_hujan_per_menit);
      address += "&curah_jam=";
      address += String(curah_hujan_per_jam);
        
      http.begin(client,address);  
      int httpCode = http.GET(); 
      String payload;

      if (httpCode > 0) { // Jika koneksi berhasil
         lcd.clear();
         payload = http.getString(); // Dapatkan payload dari tanggapan
          Serial.println(payload); // Tampilkan payload jika diperlukan
          
          int lineCount = 0; 
          int pos = 0;
          int previousPos = 0; 
          bool foundLine5 = false; 

          while ((pos = payload.indexOf('\n', pos)) != -1) {
            lineCount++; // Tambahkan nomor baris
            
              if(lineCount == 3){
                kenaikan = payload.substring(previousPos, pos);
                lcd.setCursor(0, 0);
                lcd.print(kenaikan);
            
              }else if(lineCount == 4 ){
                prediksi = payload.substring(previousPos, pos);
                lcd.setCursor(0, 1);
                lcd.print(prediksi);
              }
            previousPos = pos + 1;
            pos++;
          }  
      }else {
        Serial.println("Gagal, kode status HTTP: " + String(httpCode));
      }
      delay(3000);
    }else {
           lcd.clear();
           lcd.backlight();
           lcd.setCursor(0, 0);      
           lcd.print("--SYSTEM READY--");
           lcd.setCursor(0, 1);      
           lcd.print("PUTUS KONEKSI   ");
           Serial.print("BELOM connect\n");
        //   ESP.restart();
    }
  }
    Serial.println("\n\n");
    delay(1000); 
  //}
}

void bacaRTC()
{
  DateTime now = rtc.now(); // Ambil data waktu dari DS1307
  tanggal = String(now.day(), DEC) + "-" + String(now.month(), DEC) + "-" + String(now.year() - 2000);
  jam = konversi(String(now.hour(), DEC));
  menit = konversi(String(now.minute(), DEC));
  detik = konversi(String(now.second(), DEC));
}

String konversi(String angka) // Fungsi untuk supaya jika angka satuan ditambah 0 di depannya, Misalkan jam 1 maka jadi 01 pada LCD
{
  if (angka.length() == 1) angka = "0" + angka;
  else angka = angka;
  return angka;
}

