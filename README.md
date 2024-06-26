# Progetto di Ingegneria del Software 2023/2024

Repo per il progetto di Ing. del software a.a. 2023/2024, gruppo GC52.

## Peer Review 1

[Peer Review 1](https://polimi365-my.sharepoint.com/:w:/g/personal/10764503_polimi_it/ETClKx4a5B5Pu3p3W4UNNpYB9nnfPAPuJF9sISOBrFTyoQ)  
[Model UML docs](https://onedrive.live.com/edit?id=1F421839E4C563F3!1040&resid=1F421839E4C563F3!1040&ithint=file%2cdocx&authkey=!ABCsSRtd3BrLvgE&wdo=2&cid=1f421839e4c563f3)

## Peer Review 2

[Peer Review 2](https://polimi365-my.sharepoint.com/:w:/g/personal/10764503_polimi_it/Ebxap7Dv51VNpnaiZcBPYRUBCjxlwUEvyX-2WERFa5F_GQ?e=oVtpu9)  
[Controller & Network docs](https://polimi365-my.sharepoint.com/:w:/g/personal/10764503_polimi_it/EZ1D05vwbUdOvgHmNtuxIfgB2YLrGaG1xbsS_QZ0e1ywUw?e=oxSbQM)

## Utils

[linear](https://linear.app/ingsw2024-gc52/team/ING/all)  
[drive massimo](https://drive.google.com/drive/folders/1VBt6Vx82A4zq1yFQA5c-Ebv025igLe42)





# NUOVO README(quello vecchio lo lascio per comodità dei link) 
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

Project requirements: [todo]().

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
    java -jar GUIClient.jar
    ```
    
### Run Instructions
1. Clone this repository:
    ```shell
   git clone https://github.com/AlbertoFVisconti/ing-sw-2024-sandretti-sartori-valente-visconti
   ```
2. Move to the repository folder.

3. Move to the directory "deliverables\final\jar" and execute the server and a client:
     ```shell
    java -jar Server.jar
    java -jar TUIClient.jar
    ```
    or:
    ```shell
    java -jar Server.jar
    java -jar GUIClient.jar
    ```
