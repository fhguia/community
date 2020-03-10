package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GitHubUser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author chenzhichao
 * @date 2020/3/10 - 17:33
 */
@Component
public class GitHubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
             String string= response.body().string();
             String token=string.split("&")[0].split("=")[1];
             return token;
        }catch (Exception e) {
            e.printStackTrace();
            }
        return null;
    }
    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
