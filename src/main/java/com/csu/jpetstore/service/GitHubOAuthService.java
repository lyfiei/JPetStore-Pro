package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class GitHubOAuthService {

    @Value("${github.oauth.client-id}")
    private String clientId;

    @Value("${github.oauth.client-secret}")
    private String clientSecret;

    @Value("${github.oauth.redirect-uri}")
    private String redirectUri;

    @Autowired
    private AccountMapper accountMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_API = "https://api.github.com/user";
    private static final String EMAILS_API = "https://api.github.com/user/emails";

    public String getAuthorizationUrl() {
        return UriComponentsBuilder.fromHttpUrl(AUTH_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "user:email")
                .toUriString();
    }

    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("code", code);
        body.put("redirect_uri", redirectUri);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("access_token")) {
            return (String) response.getBody().get("access_token");
        }
        throw new RuntimeException("GitHub 授权失败");
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> userResp = restTemplate.exchange(USER_API, HttpMethod.GET, request, Map.class);
        Map<String, Object> user = userResp.getBody();

        ResponseEntity<List> emailResp = restTemplate.exchange(EMAILS_API, HttpMethod.GET, request, List.class);
        List<Map<String, Object>> emails = emailResp.getBody();

        String email = null;
        if (emails != null) {
            email = emails.stream()
                    .filter(e -> Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified")))
                    .map(e -> (String) e.get("email"))
                    .findFirst()
                    .orElse(null);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("login", user.get("login"));
        result.put("name", user.get("name"));
        result.put("email", email);
        result.put("avatarUrl", user.get("avatar_url"));
        return result;
    }

    public Account findOrCreateAccount(Map<String, Object> githubUser) throws Exception {
        String githubLogin = (String) githubUser.get("login");
        String email = (String) githubUser.get("email");
        String name = (String) githubUser.get("name");

        // 优先用邮箱查找已有账号
        if (email != null) {
            Account existingByEmail = accountMapper.getAccountByEmail(email);
            if (existingByEmail != null) {
                return existingByEmail;
            }
        }

        // 检查 GitHub 用户名是否已被注册
        Account existingByUsername = accountMapper.getAccountByUsername(githubLogin);
        if (existingByUsername != null) {
            return existingByUsername;
        }

        // 创建新账号
        Account account = new Account();
        account.setUsername(githubLogin);
        account.setPassword(UUID.randomUUID().toString().substring(0, 12));
        account.setEmail(email != null ? email : githubLogin + "@github.user");
        account.setFirstName(name != null ? name : githubLogin);
        account.setLastName("");
        account.setPhone("");
        account.setAddress1("");
        account.setAddress2("");
        account.setCity("");
        account.setState("");
        account.setZip("");
        account.setCountry("");
        account.setLanguagePreference("zh_CN");
        account.setFavouriteCategoryId("FISH");
        account.setListOption(true);
        account.setBannerOption(true);

        accountMapper.insertAccount(account);
        accountMapper.insertProfile(account);
        accountMapper.insertSignon(account);

        return account;
    }
}
