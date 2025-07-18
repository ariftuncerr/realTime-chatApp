# Real-Time Chat Application (Kotlin + Firebase + MVVM)

This project is a real-time chat application developed with Kotlin, structured using the MVVM (Model-View-ViewModel) architectural pattern. The application leverages Firebase Authentication for secure user login and Firestore as a real-time NoSQL database for storing user and message data. Users can register with email or Google, view chat channels, and communicate instantly through a responsive and scalable message system.

## 📌 Project Goals

- Build a clean and scalable Android architecture using MVVM.
- Implement real-time one-to-one messaging functionality.
- Integrate Firebase services for seamless backend operations.
- Deliver an intuitive and responsive user interface.

## 🔧 Technologies Used

- **Programming Language**: Kotlin
- **Architecture**: MVVM (ViewModel, LiveData, Repository)
- **Backend**: Firebase Authentication & Firestore
- **UI Components**: RecyclerView, ViewBinding, Material Components
- **Async Handling**: Kotlin Coroutines & Flow
- **Navigation**: Android Jetpack Navigation Component

## 🧩 Features

- Google and Email/Password based registration & login
- One-to-one real-time chat between users
- Conversations listed with other users
- Chat screen with message delivery and timestamp
- Firestore-triggered real-time data updates
- MVVM separation for testable and maintainable code
- Clean UI with Material Design and ViewBinding

## 📁 Project Structure

<pre> com.ariftuncer.realtime_chatapp
│
├── data/
│   ├── model/              → Data classes (User, Message)
│   └── repository/         → Firestore & Auth operations
│
├── ui/
│   ├── auth/               → LoginActivity, RegisterActivity
│   ├── main/               → HomeFragment, MessagesFragment, SettingsFragment
│   ├── chat/               → ChatActivity, MessageAdapter
│   └── adapters/           → Reusable RecyclerView adapters
│
├── viewmodel/              → AuthViewModel, ChatViewModel, MainViewModel
│
└── utils/                  → Firestore helpers, extensions
 </pre>

 ##  📁 Firabase Structure
 <pre>
users (collection)
    └── {uid} (document)
          ├── username: String
          ├── email: String

direct_messages (collection)
└── {uid1_uid2} (document)
    ├── users: [uid1, uid2]
    └── messages (subcollection)
        ├── {messageId} (document)
            ├── senderId: String
            ├── message: String
            └── timestamp: Timestamp

 </pre>


## 🚀 Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/RealtimeChatApp.git
   cd RealtimeChatApp



