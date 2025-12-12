# 📡 WebSocket - SpringBoot

A real-time **WebSocket-based backend application** built using **Spring Boot**, enabling fast and bidirectional communication between clients and the server.

This project demonstrates how to implement **WebSockets with STOMP protocol** in Spring Boot, making it suitable for chat applications, live notifications, and real-time systems.

---

## 🚀 Key Highlights

- 🔁 Real-time, bidirectional communication
- ⚡ Built with Spring Boot & WebSocket
- 📬 STOMP-based messaging
- 🧩 Clean and modular project structure
- 🐳 Docker-ready backend
- 🧪 Easy to test using Postman or WebSocket clients

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot
- **Messaging:** WebSocket, STOMP
- **Build Tool:** Maven
- **Protocol:** WebSocket
- **Containerization:** Docker
- **Testing:** Postman, WebSocket clients

---


## 🧱 Project Structure

WebSocketSpringBoot/
├── src
│ ├── main
│ │ ├── java
│ │ │ ├── config # WebSocket & STOMP configuration
│ │ │ ├── controller # Message handling controllers
│ │ │ └── model # Message DTOs / entities
│ │ └── resources
│ │ └── application.yml
├── Dockerfile
├── pom.xml
└── README.md


---

## ⚙️ Getting Started

### ✅ Prerequisites

- Java 17+
- Maven
- Any WebSocket-capable client (browser / Postman / frontend)

---

### 📥 Installation

bash
git clone https://github.com/Shashankcode9/WebSocketSpringBoot.git
cd WebSocketSpringBoot
mvn clean install
mvn spring-boot:run

Access It At

http://localhost:8080

---

### 🔄 How WebSocket Flow Works

---

Client establishes a WebSocket connection

Messages are sent using STOMP

Server processes and broadcasts messages

Subscribed clients receive updates instantly

Client → WebSocket → STOMP → Spring Boot → Broadcast → Clients

---

### 🔗 WebSocket Endpoints

---

Type	Endpoint	Description
WebSocket	/ws	WebSocket connection endpoint
Subscribe	/topic/messages	Receive broadcast messages
Send	/app/sendMessage	Send message to server


---

### 🖥️ Frontend Integration Example

---

const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  stompClient.subscribe('/topic/messages', (msg) => {
    console.log(msg.body);
  });

  stompClient.send('/app/sendMessage', {}, JSON.stringify({
    sender: 'User',
    content: 'Hello WebSocket!'
  }));
});

---

### 🧪 Testing

---

Postman (WebSocket tab)

WebSocket King Client

Browser with STOMP.js

Any frontend framework (React / Flutter / JS)

---

### 🐳 Docker Support

---

docker build -t websocket-springboot .
docker run -p 8080:8080 websocket-springboot

---

### 📈 Use Cases

---

Real-time chat applications

Live notification systems

Real-time dashboards

Multiplayer or collaborative apps

---

### 🔮 Future Enhancements

---

Private/user-specific messaging

Message persistence

Frontend UI integration

CI/CD deployment


---

### 🤝 Contributing

---

Contributions are welcome!

Fork the repository

Create a new branch

Commit your changes

Open a Pull Request

---

### 👨‍💻 Author

---

Shashank Chaurasiya

GitHub: https://github.com/Shashankcode9


