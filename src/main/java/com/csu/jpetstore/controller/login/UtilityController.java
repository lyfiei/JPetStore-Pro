package com.csu.jpetstore.controller.login;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
public class UtilityController {

    @GetMapping("/captchaForRegister")
    public void getCaptcha(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int width = 100;
        int height = 40;

        // 1. 创建画布
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 2. 【关键】填充背景色，否则就是黑色
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        // 3. 随机生成验证码字符串
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char c = chars.charAt(r.nextInt(chars.length()));
            sb.append(c);
        }
        String code = sb.toString();

        // 4. 将验证码存入 Session (注意：RegisterController 里的校验会用到这个 key)
        HttpSession session = req.getSession();
        session.setAttribute("captcha", code);

        // 5. 绘制验证码字符
        g.setFont(new Font("Arial", Font.BOLD, 22));
        for (int i = 0; i < 4; i++) {
            // 随机颜色
            g.setColor(new Color(r.nextInt(200), r.nextInt(100), r.nextInt(200)));
            g.drawString(String.valueOf(code.charAt(i)), 20 * i + 10, 28);
        }

        // 6. 绘制干扰线，防止 OCR 识别
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        }

        // 7. 输出图片
        resp.setContentType("image/jpeg");
        // 禁止浏览器缓存验证码
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);

        ServletOutputStream responseOutputStream = resp.getOutputStream();
        ImageIO.write(image, "jpg", responseOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}