import React from "react";
import "./Contact.scss";
import firebase from 'firebase/app';
import 'firebase/firestore';

// Initialize Firebase if not already done
import { db } from '../../../firebase-config/config';

class ContactForm extends React.Component {
    sendMessage = async (event) => {
        event.preventDefault(); // Prevent default form submission behavior

        const nameInput = document.getElementById("uname");
        const emailInput = document.getElementById("email");
        const messageInput = document.getElementById("message");

        const name = nameInput.value;
        const email = emailInput.value;
        const message = messageInput.value;

        if (!name || !email || !message) {
            alert("Name, email, and message are required.");
            return;
        }

        const formData = {
            uname: name,
            email: email,
            message: message,
        };

        try {
            const querySnapshot = await db.collection("users").where("email", "==", email).get();

            if (querySnapshot.size >= 10) {
                alert("Sufficient amount of emails with the same name already exists.");
                return;
            }
            // Saving the form data to Firebase
            await db.collection("contactus").doc(email).set(formData);

            alert("Message Sent");
            
            // Clear input fields after message is sent
            nameInput.value = '';
            emailInput.value = '';
            messageInput.value = '';
        } catch (error) {
            console.error("Error sending message:", error);
            alert("Error sending message. Please try again later.");
        }
    };

    render() {
        return (
            <div className="contact-us-section">
                <div className="glassmorphism-effect">
                <div className="contact-us-content">
                    <div className="right-content">
                        <span className="small-text">Contact Us</span><br />
                        <span className="big-text">Get in touch with us!</span>
                        <div className="text">We'll get back to you as soon as possible.</div>
                    </div>
                    <div className="left-content">
                        <form onSubmit={this.sendMessage}>
                            <div className="input-row">
                            <div className="contact-input-box">
                                <input id="uname" type="text" placeholder="Name" />
                            </div>
                            <div className="contact-input-box">
                                <input id="email" type="email" placeholder="Email Address" />
                            </div>
                            </div>
                            <div className="full-width-input">
                            <div className="contact-input-box-message">                                 
                                <input id="message" type="text" placeholder="Enter your Message" style={{ paddingTop: '25px', paddingBottom: '25px' }} />  
                                </div>
                            </div>
                            <div className="button">
                                <input
                                    type="button"
                                    value="Send Message"
                                    onClick={this.sendMessage}
                                />
                            </div>
                        </form>
                    </div>
                </div>
                </div>
                
            </div>
        );
    }
}

export default ContactForm;
