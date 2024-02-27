package com.yi.spring.OAuth2;

import com.yi.spring.entity.User;
import com.yi.spring.repository.UserRepository;
import com.yi.spring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class OAuth2MemberService extends DefaultOAuth2UserService {
    @Autowired
    private OAuth2LoginUserRepository oAuthLoginUserRepository;
    @Autowired
    UserRepository userRepository;
    private List<OAuth2LoginUser> reserveSaveUsers = new ArrayList<>();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user2 = super.loadUser(userRequest);
        Map<String, Object> attributes = user2.getAttributes();
//        log.info("ATTR INFO : {}", attributes.toString());

        String oauthType = userRequest.getClientRegistration().getRegistrationId();
        //oauth 타입에 따라 데이터
        String email = switch (oauthType.toLowerCase()) {
            case "kakao" -> ((Map<String, Object>) attributes.get("kakao_account")).get("email").toString();
            case "google" -> attributes.get("email").toString();
            case "naver" -> ((Map<String, Object>) attributes.get("response")).get("email").toString();
            default -> null;
        };

        if (getUserByEmailAndOAuthType(email, oauthType)==null){
//            log.info("{}({}) NOT EXISTS. REGISTER", email, oauthType);
            OAuth2LoginUser user = new OAuth2LoginUser();
            user.setHashValue( user2.getName().hashCode() );
            user.setEmail( email );
            user.setOauthType( oauthType );
            user.setAttributes( attributes.toString() );
            save(user);
        }

        return super.loadUser(userRequest);
    }

    public void save(OAuth2LoginUser user) {
        reserveSaveUsers.add( user );

////        OAuth2LoginUser result = o2userRepository.save(user);
//        OAuth2LoginUser test = new OAuth2LoginUser();
//        test.setHashValue( 1234 );
//        try {
//            OAuth2LoginUser result = oAuthLoginUserRepository.save(test);
//            System.out.println( result );
//        }catch ( Exception e ) {
//            System.out.println( "asfdsfsdfsdf ");
//            e.printStackTrace();
//        }
    }

    public void saveAll() {
        oAuthLoginUserRepository.saveAll(reserveSaveUsers);
        for ( OAuth2LoginUser member : reserveSaveUsers )
        {
            User user = new User();

            String attributes = member.getAttributes();

//            Matcher matcher = Pattern.compile(
//                    "name=[^,]+"
//            ).matcher( attributes );
//            if ( matcher.find() )
//                user.setUserName( matcher.group());
//
//            matcher = Pattern.compile(
//                    "email=[^,]+"
//            ).matcher( attributes );
//            if ( matcher.find() )
//               user.setUserId(matcher.group());

//            mapSetFunction.put("name", user::setUserName);
//            mapSetFunction.put("email", user::setUserId);
//            Map<String, Consumer<String>> mapSetFunction = Map.of( //new HashMap<>();
            List<Map.Entry<String, Consumer<String>>> listSetFunction = Arrays.asList(
                    new AbstractMap.SimpleEntry<>("nickname", user::setUserName),
                    new AbstractMap.SimpleEntry<>("email", user::setUserId),
                    new AbstractMap.SimpleEntry<>("tel", user::setUserTel),
                    new AbstractMap.SimpleEntry<>("email", user::setUserEmail)
            );
            for (Map.Entry<String, Consumer<String>> entry : listSetFunction) {
                Matcher matcher = Pattern.compile(
                        "[{, ]"+entry.getKey()+"=([^,} ]+)"
            ).matcher( attributes );
            if ( matcher.find() )
                entry.getValue().accept( matcher.group(1));
            }
            user.setUserAuth( "1" );
            user.setUserPassword( Base64.getEncoder().encodeToString( attributes.substring(0, 20).getBytes() ));
            userRepository.save( user );
        }

        reserveSaveUsers.clear();
    }

    public OAuth2LoginUser getUserByEmailAndOAuthType(String email, String oauthType){
        return oAuthLoginUserRepository.findByEmailAndOauthType(email, oauthType).orElse(null);
    }

    public User findUser(Principal principal){
        String userId = "";
        if ( principal instanceof OAuth2AuthenticationToken )
        {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)principal;
            userId = (String) ((Map)token.getPrincipal().getAttribute( "kakao_account" )).get( "email" );
        }
        else
        {
            userId = principal.getName();
        }

        return userRepository.findByUserId( userId ).orElse( null );
    }



}
