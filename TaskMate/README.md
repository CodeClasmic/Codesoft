# 📋 TaskMate - To-Do List Android App

TaskMate is a powerful and intuitive to-do list Android application designed to help users organize their tasks efficiently. It allows users to add, view, and manage tasks with features like high-priority marking, due date selection, and persistent local storage. Built with **Android Studio**, TaskMate offers a clean user interface with dark mode support and dynamic task management.

---

## 🚀 Features

- ✅ **Add, View, and Delete Tasks**: Easily manage your daily to-dos.
- 🔴 **High-Priority Tasks**: Highlight important tasks in red. High-priority tasks always appear at the top of the list and cannot be changed later.
- 🕒 **Due Date & Time Picker**: Optional date and time selection for each task.
- 🌑 **Dark Mode Support**: Toggle between light and dark themes for comfortable viewing.
- 💾 **Persistent Local Storage**: Tasks and their priorities persist even after the app is closed.
- 🏃 **Responsive UI**: Smooth user experience with intuitive navigation and UI elements.

---



## 🛠️ Technologies Used

- **Java** for core functionality
- **XML** for UI design
- **Android Studio** for development
- **RecyclerView** for dynamic task display
- **SharedPreferences** for local data persistence

---



## ✍️ Usage

1. **Add Task**: Click the ➕ button to open the add task dialog. Fill in the details, select due date/time (optional), and choose high priority if needed.

2. **View Task Details**: Click on a task title to view and edit details.

3. **Toggle Dark Mode**: Use the dark mode switch to change themes.

4. **Persistent Tasks**: Close and reopen the app; tasks remain saved with their priorities and details.

---

## 🎨 Project Structure

```
TaskMate/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/projects/taskmate/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── Adapter.java
│   │   │   │   ├── task.java
│   │   │   │   └── TaskStorage.java
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_main.xml
│   │   │       │   └── dialog_add_task.xml
│   │   │       └── values/
│   │   │           ├── colors.xml
│   │   │           ├── strings.xml
│   │   │           └── themes.xml
│   └── build.gradle
└── README.md
```

---

## 🐛 Known Issues

- Date and time picker currently allows future date selection only.
- Priority toggle is immutable after task creation by design.
- Minor UI adjustments needed for landscape orientation.

---

## 💡 Future Improvements

- ⏰ **Task Reminders**: Add push notifications for due tasks.
- 🌐 **Cloud Sync**: Sync tasks across multiple devices.
- 🎨 **Custom Themes**: Allow users to personalize UI themes.
- 📝 **Task Categories**: Group tasks by customizable categories.
- 🔔 **Recurring Tasks**: Support for repeating tasks daily/weekly.

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch: `
3. Commit your changes: `
4. Push to the branch: `
5. Submit a pull request.

---

## ⚖️ License

This project is licensed under the (LICENSE).

---



⭐ **If you like TaskMate, give it a star on GitHub!** ⭐

