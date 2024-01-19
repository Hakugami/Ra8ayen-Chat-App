CREATE TABLE UserAccounts (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    PhoneNumber VARCHAR(255) UNIQUE NOT NULL,
    DisplayName VARCHAR(255) NOT NULL,
    EmailAddress VARCHAR(255) UNIQUE NOT NULL,
    ProfilePicture MEDIUMBLOB,
    PasswordHash VARCHAR(255) NOT NULL,
    Gender ENUM('Male', 'Female') NOT NULL,
    Country VARCHAR(255) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Bio TEXT,
    UserMode ENUM('Available', 'Busy', 'Away') DEFAULT 'Away',
    UserStatus ENUM('Online', 'Offline') DEFAULT 'Offline', 
    LastLogin DATETIME
);

CREATE TABLE UserContacts (
    FriendID INT NOT NULL,
    UserID INT NOT NULL,
    CreationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES UserAccounts(UserID),
    FOREIGN KEY (FriendID) REFERENCES UserAccounts(UserID),
    PRIMARY KEY(FriendID, UserID)
);

CREATE TABLE Chat (
    ChatID INT AUTO_INCREMENT PRIMARY KEY,
    ChatName VARCHAR(255) NOT NULL,
    ChatImage MEDIUMBLOB,
    AdminID INT,
    CreationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    LastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AdminID) REFERENCES UserAccounts(UserID)
);

CREATE TABLE Messages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,
    SenderID INT NOT NULL,
    ReceiverID INT NOT NULL,
    MessageContent TEXT NOT NULL,
    MessageTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IsAttachment BOOLEAN,
    FOREIGN KEY (SenderID) REFERENCES UserAccounts(UserID),
    FOREIGN KEY (ReceiverID) REFERENCES Chat(ChatID)
);

CREATE TABLE Attachment (
	AttachmentID INT AUTO_INCREMENT PRIMARY KEY,
    MessageID INT NOT NULL,
    Attachment LONGBLOB NOT NULL,
    FOREIGN KEY (MessageID) REFERENCES Messages(MessageID)
);

CREATE TABLE ChatParticipants (
    ChatID INT NOT NULL,
    ParticipantUserID INT NOT NULL,
    ParticipantStartDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(ChatID, ParticipantUserID),
    FOREIGN KEY (ChatID) REFERENCES Chat(ChatID),
    FOREIGN KEY (ParticipantUserID) REFERENCES UserAccounts(UserID)
);

CREATE TABLE UserNotifications (
    NotificationID INT AUTO_INCREMENT PRIMARY KEY,
    ReceiverID INT NOT NULL,
    SenderID INT NOT NULL,
    NotificationMessage TEXT,
    NotificationSentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ReceiverID) REFERENCES UserAccounts(UserID),
    FOREIGN KEY (SenderID) REFERENCES UserAccounts(UserID)
);


