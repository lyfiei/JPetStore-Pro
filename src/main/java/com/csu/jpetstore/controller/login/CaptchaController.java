package com.csu.jpetstore.controller.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * 验证码控制器 - 合并了所有验证码生成功能
 */
@Controller
public class CaptchaController {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;

    /**
     * 通用验证码生成（用于登录）
     */
    @GetMapping("/captcha")
    protected void generateCaptcha(HttpServletRequest req, HttpServletResponse resp) {
        generateCaptchaImage(req, resp);
    }

    /**
     * 注册专用验证码
     */
    @GetMapping("/captchaForRegister")
    protected void generateCaptchaForRegister(HttpServletRequest req, HttpServletResponse resp) {
        generateCaptchaImage(req, resp);
    }

    /**
     * 统一的验证码生成逻辑
     */
    private void generateCaptchaImage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();

            // 设置背景
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // 随机生成验证码
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random r = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                char c = chars.charAt(r.nextInt(chars.length()));
                sb.append(c);
            }
            String code = sb.toString();

            // 将验证码保存到 Session
            HttpSession session = req.getSession();
            session.setAttribute("captcha", code);

            // 绘制验证码字符
            g.setFont(new Font("Arial", Font.BOLD, 22));
            for (int i = 0; i < 4; i++) {
                g.setColor(new Color(r.nextInt(200), r.nextInt(100), r.nextInt(200)));
                g.drawString(String.valueOf(code.charAt(i)), 20 * i + 10, 28);
            }

            // 加干扰线
            for (int i = 0; i < 5; i++) {
                g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                g.drawLine(r.nextInt(WIDTH), r.nextInt(HEIGHT), r.nextInt(WIDTH), r.nextInt(HEIGHT));
            }

            g.dispose();

            // 输出到浏览器
            resp.setContentType("image/jpeg");
            resp.setHeader("Pragma", "No-cache");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setDateHeader("Expires", 0);
            
            ImageIO.write(image, "jpeg", resp.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
