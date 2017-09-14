///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.'
// * Author: Sean Casaus
// */

package team4_finalproject;

import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Team4Ghost extends JLabel implements Runnable {
    private static int ghostWidth = 0;
    private static int ghostHeight = 0;
    private static JLabel bubbleLabel;



    public Team4Ghost() { //Constructor
        ghostWidth = 49;
        ghostHeight = 54;
        ImageIcon newImage = new ImageIcon(this.getClass().getResource("Team4Images/Pac-Man-Ghost-PNG-Transparent-Image.png"));
        setIcon(new ImageIcon(newImage.getImage().getScaledInstance(ghostWidth, ghostHeight, 0))); //scales the image
    }

    public Team4Ghost(String answer) { //Constructor
        ghostWidth = 350;
        ghostHeight = 250;
        String temp = answer;
        bubbleLabel = new JLabel(temp);
        bubbleLabel.setSize(300,15);
        bubbleLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        bubbleLabel.setLocation(50,75);
        ImageIcon newImage = new ImageIcon(this.getClass().getResource("Team4Images/speechBubble.png"));
        this.setIcon(new ImageIcon(newImage.getImage().getScaledInstance(ghostWidth, ghostHeight, 0))); //scales the image
        this.add(bubbleLabel);






    }


    //Basic Algorithm that moves the ghost along the x axis at the bottom of a panel
    @Override
    public void run() {
        int x = 0;
        int y = 0; //default y coordinate
        int Speed = 5; //Change Speed of Ghost
        int xSpeed = Speed;
        int ySpeed = Speed;
        int width = 1000 - ghostWidth; //Difference between width of panel and width of ghost
        int height = 400 - ghostHeight; //The height the ghost can travel

        while (true) {
            setBounds(x, y, ghostWidth, ghostHeight);
            if (x < 0) {
                xSpeed = Speed;
            }
            if (x > width) {
                xSpeed = -Speed;
            }
            if (y < 0) {
                ySpeed = Speed;
            }
            if (y > height) {
                ySpeed = -Speed;
            }
            x += xSpeed;
            y += ySpeed;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Error");
            }
        }
    }
}

