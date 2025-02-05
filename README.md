```
░░      ░░░░      ░░░       ░░░       ░░░        ░░░      ░░░░      ░░░       ░░░   ░░░  ░
▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒▒▒▒  ▒▒▒▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒    ▒▒  ▒
▓  ▓▓▓▓▓▓▓▓  ▓▓▓▓  ▓▓       ▓▓▓       ▓▓▓▓▓▓  ▓▓▓▓▓  ▓▓▓▓▓▓▓▓  ▓▓▓▓  ▓▓       ▓▓▓  ▓  ▓  ▓
█  ████  ██        ██  ████████  ███  ██████  █████  ████  ██  ████  ██  ███  ███  ██    █
██      ███  ████  ██  ████████  ████  ██        ███      ████      ███  ████  ██  ███   █

                                        ░        ░░   ░░░  ░░░      ░░░        ░░         ░
                                        ▒  ▒▒▒▒▒▒▒▒    ▒▒  ▒▒  ▒▒▒▒▒▒▒▒▒▒▒  ▒▒▒▒▒▒▒▒▒  ▒  ▒
                                        ▓      ▓▓▓▓  ▓  ▓  ▓▓▓      ▓▓▓▓▓▓  ▓▓▓▓▓▓▓▓  ▓▓  ▓
                                        █  ████████  ██    ████████  █████  ██████  ███████
                                        █        ██  ███   ███      ██████  █████         █                                                                                          
```                                                                                       

By<br/>
Raphaël Azou<br/>
Goulven Le Pennec<br/>
M2 ASSEL<br/>
ENSTA (Brest)

## 1. Configure the project:
clone the project <br/>
```
git clone https://github.com/GoulvenLP/capricorn.git
```
- Open intelliJ $\rightarrow$ 'open' [your project path]
- Set jdk version: IDE project settings/ project structure/ $\rightarrow$ chose a v 21 SDK
- Go to `capricorn/dependency`: right click on the yasson-2.0.2.jar: "add as library"

You are ready to start!

## 2. System:
- Radar detects targets at 100km with a refreshing period of 5s
- The radar system stores all detected engines into a list.
- The history of the positions of a detected engine is saved for 10 points

## 3 - Command Center
- Decision is taken in 2s. It engages a missile. The missile is fired only when the target it is at range of this missile

## 4 - Missile
- Range: 50km
- Speed: 1500 km/h

Trajectory updated on each radar refresh cycle.<br/>
The missile has a detection range of 10km: takes the relay on the radar if the target is at range. Refresh cycle is 50ms at this time.
If the target is missied, another missile can be launched (after decision of the command center)

Target missied in 1/10 cases.

## 5 - Threat
- Speed constant for each simulation: 200-300 km/h (poisson)
- Explosives: 500 km.
- Action: Kamikaze
- proba of failing destruction: 1/10

Between 3 and 5 threats per simulation.<br/>
All threats have the same speed during a simulation.<br/>
Average arrival of each threat: 5 minutes, max delay = 10 min. (poisson)