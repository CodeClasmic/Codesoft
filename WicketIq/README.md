# WicketIQ - Cricket Quiz App

WicketIQ is an interactive **Cricket Quiz App** designed for cricket enthusiasts. The app provides multiple categories of cricket-related quizzes with a **10-second timer** per question, real-time scoring updates, and a leaderboard feature.

## 📌 Features
- **Multiple Cricket Quiz Categories** (IPL, T20, ODI, Test, Legends, etc.)
- **Time-based Questions** (10-second countdown per question)
- **Leaderboard System** (Top 10 players displayed)
- **User Authentication** (Email-based login via Firebase Authentication)
- **Firestore Database Integration** (User data and leaderboard stored in Firestore)
- **Visually Appealing UI** (Grid layout for categories, intuitive navigation)
- **Score Tracking & Updates** (Scores are updated after each quiz session)
- **One Attempt Per Question** (Users can only attempt each question once per session)

## 📥 Installation & Setup
1. **Clone the Repository**
   ```sh
   git clone https://github.com/your-username/WicketIQ.git
   cd WicketIQ
   ```
2. **Open in Android Studio**
   - Launch **Android Studio**
   - Open the **WicketIQ** project
3. **Setup Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable **Authentication (Email/Password)**
   - Enable **Firestore Database**
   - Download and add `google-services.json` to `app/` directory
4. **Run the App**
   - Connect an **Android device** or use an **emulator**
   - Click **Run ▶** in Android Studio

## 🎯 How "Attempt Only Once" Works
Each question can be **attempted only once** using the following logic:
- When a user selects an option, all other options are **disabled** to prevent multiple attempts.
- If the user **skips or times out**, the app automatically moves to the next question.
- Once a question is **answered or skipped**, it **cannot be revisited** in the same session.
- A message is displayed if all questions have been attempted: **"All questions attempted! New will be added soon."**

## 🛠️ Tech Stack
- **Language:** Java (Android)
- **Framework:** Android SDK
- **Database:** Firebase Firestore
- **Authentication:** Firebase Auth
- **UI Design:** XML (RelativeLayout, Buttons, TextViews)

## 🤝 Contribution
Want to contribute? Follow these steps:
1. **Fork** the repo
2. **Create a new branch**: `git checkout -b feature-branch`
3. **Make changes & commit**: `git commit -m "Added new feature"`
4. **Push to GitHub**: `git push origin feature-branch`
5. **Create a Pull Request**

---
🚀 **Enjoy playing WicketIQ and test your cricket knowledge!** 🏏

