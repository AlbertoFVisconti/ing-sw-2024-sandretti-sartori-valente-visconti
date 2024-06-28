
# Codex Naturalis Board Game - Software Engineering Project

<img src="https://www.craniocreations.it/storage/media/products/19/41/Codex_scatola+ombra.png" width="260" align="right" />

Codex Naturalis Board Game is the final test of **"Software Engineering"** course of **"Engineering of Computing Systems"** held at Politecnico di Milano (2023/2024).

Final Grade: -/30L

**Teacher**: CUGOLA GIANPAOLO SAVERIO

## The Team
* [Massimo Sandretti](https://github.com/MassimoSandre)
* [Cristiano Sartori](https://github.com/Eieusis)
* [Mario Valente](https://github.com/mavio9)
* [Alberto Visconti](https://github.com/AlbertoFVisconti)

## Project specification
The project consists of a Java version of the board game *Codex* *Naturalis*, made by Cranio Creations. You can find the real game [here](https://www.craniocreations.it/prodotto/codex-naturalis).

Project requirements: [here](https://github.com/AlbertoFVisconti/ing-sw-2024-sandretti-sartori-valente-visconti/blob/master/src/main/resources/requirements.pdf).

## Implemented functionalities

### Main functionalities
| Functionality                    | Status |
|:---------------------------------|:------:|
| Complete rules                   |   ✅    |
| RMI                              |   ✅    |
| Socket                           |   ✅    |
| CLI _(Command Line Interface)_   |   ✅    |
| GUI _(Graphical User Interface)_ |   ✅    |


### Advanced functionalities
| Functionality                | Status |
|:-----------------------------|:------:|
| Chat                         |   ✅    |
| Simultaneous games           |   ✅    |
| Persistence                  |   ✅    |
| Resilience to disconnections |   ✅    |


✅ Implemented


## Usage

### Requirements

Regardless of the operating system, you must have installed the following programs:
- Java jdk 21
- Maven 

#### Windows
On Windows it is needed to:
- Set system visual scaling to 100%.

### Compile Instructions
1. Clone this repository:
    ```shell
   git clone https://github.com/AlbertoFVisconti/ing-sw-2024-sandretti-sartori-valente-visconti
   ```
2. Download the folder 'image' [here](https://drive.google.com/drive/folders/12qo1t89ZFsH78X0PE5vJz66rhZUH4jST),unzip it and move it in src/main/resources
3. Move to the repository folder.
4. Build the code with maven and move the jar files from `target` to a new directory of your choice:
    ```shell
    mvn clean package 
    ```
5. Move to the that directory and execute the server and/or the client:
    ```shell
    java -jar Server.jar
    java -jar TUIClient.jar
    ```
    or:
    ```shell
    java -jar Server.jar
    java -jar GUIClientMacOS.jar
    ```
   or:
    ```shell
    java -jar Server.jar
    java -jar GUIClientWindows.jar
    ```
    
### Run Instructions
1. Clone this repository:
    ```shell
   git clone https://github.com/AlbertoFVisconti/ing-sw-2024-sandretti-sartori-valente-visconti
   ```
2. Move to the repository folder.

3. Move to the directory "deliverables\jar" and execute the server and a client:
     ```shell
    java -jar Server.jar
    java -jar TUIClient.jar
    ```
    or:
    ```shell
    java -jar Server.jar
    java -jar GUIClientMacOS.jar
    ```
   or:
    ```shell
    java -jar Server.jar
    java -jar GUIClientWindows.jar
    ```
