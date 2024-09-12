import React from 'react';
import './styles/Menu.css';

function Menu() {
  return (
    <nav className="menu">
      <ul>
        <li><a href="#home">Home</a></li>
        <li><a href="#about">About</a></li>
        <li><a href="#services">Services</a></li>
        <li><a href="#contact">Contact</a></li>
        <li><a href="#profile">Profile</a></li>
      </ul>
    </nav>
  );
}

export default Menu;
