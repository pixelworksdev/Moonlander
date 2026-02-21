# MoonLander

## Project Overview

**MoonLander** is a small 2D game where the player controls a lunar lander to safely touch down on the moon's surface.  
The game combines **physics simulation**, **resource management (fuel)**, player controls, and an optional **autopilot feature** to create a fun and challenging experience.

---

## Gameplay

- The player controls the lander using **W/A/S/D** keys.
- **Thrust consumes fuel**; fuel is limited and crucial for success.
- The game ends if the lander **crashes** (impact velocity too high) or **runs out of fuel**.
- Upon landing or crash, the game shows a **clear Game Over status** and pauses.
- **Restart** is always possible with the **R key**.

---

## Features

- **Physics & Gravity** – The lander accelerates due to gravity and thrust.
- **Fuel System** – Fuel is consumed when using the thrusters; running out ends the game.
- **Game Status Indicators**:
    - `"LANDED SUCCESSFULLY"` for gentle landings
    - `"GAME OVER - CRASHED"` for crashes
- **Gameplay Timer** – HUD shows the elapsed game time.
- **Restart Function** – Restart the game after Game Over or a successful landing.
- **Debug Mode (optional)** – Displays velocity, fuel, and hitboxes for development purposes.

---

## Optional / Expandable Features

- **Autopilot** – Automatically controls vertical speed.
- **Autopilot Toggle** – Can be turned on/off during gameplay.
- **7-Segment Display** – Optional HUD element for fuel, time, or height.

---

## Technical Implementation

- **Language:** Java
- **Graphics/UI:** Graphics2D
- **Architecture:** MVC-like
    - **Game** → Controller / Main Logic
    - **MoonLander** → Model (Physics, Status, Fuel)
    - **FuelTank** → View / Fuel Display
- **Game State Management:** RUNNING, LANDED, CRASHED
- **Physics:** Vector-based position, velocity, and acceleration

---

## Learning Goals

- Working with **2D physics** and vectors
- **Resource management** (fuel consumption)
- Implementing a **game loop** with update & draw methods
- **Game state management** and pausing
- HUD elements, **debugging**, and restart functionality
- Optional extensions: **autopilot** and enhanced UI features

---