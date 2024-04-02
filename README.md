<div align="center">
  <h2>예약 맛집 프로젝트</h2>
</div>

## 목차
- [개요](#개요)
- [프로젝트 설명](#프로젝트-설명)

## 개요
- **프로젝트 이름:** Yemaat
- **프로젝트 인원:** 5명
- **프로젝트 설명:**  
  상세한 맛집 데이터로 다채로운 맛집 정보 제시를 하고 업태별, 순위별, 메뉴별, 위치별 등 다양한 검색 조건을 통해 꼭 맞는 맛집을 추천하는 사이트입니다. 또한, 실시간 정보 제공 및 예약 기능을 제공합니다.
- **프로젝트 기간:** 2달
- **URL:** [Yemaat](http://docs.yi.or.kr:3015/home)
- **개발 환경:**  
  ![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)
  ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
  ![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
- **사용 기술:**
  - Spring Boot 기반
  
  ![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
  ![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)
  ![HTML](https://img.shields.io/badge/HTML-239120?style=for-the-badge&logo=html5&logoColor=white)
  ![CSS](https://img.shields.io/badge/CSS-239120?&style=for-the-badge&logo=css3&logoColor=white)
  ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)
  ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
  ![jQuery](https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white)

![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=Hyeonim)

## 데이터베이스 구조
![Database Structure](https://github.com/Hyeonim/yemaat/assets/122865860/37b0848a-5224-4038-9596-dfbfdf1ffe59)

## 마인드 맵
![Mind Map](https://github.com/Hyeonim/yemaat/assets/122865860/fe5e91e2-1755-4798-b0d5-0a24b7d95b8c)

## 프로젝트 구성화면

### **[메인 화면]**
![Main Screen](https://github.com/Hyeonim/yemaat/assets/122865860/571bbdf2-0ad1-4b3e-ab96-ee7f3e789956)

- 공지사항, 이용약관, 이벤트를 확인 가능
- 예약 많은 순, 추천순, 카테고리 별로 가게를 추천
- 사용자의 권한 별로 마이페이지, 가게 페이지, 관리자 페이지 이용 가능
- 검색 기능을 사용하여 원하는 식당을 찾을 수 있음

### **[검색 화면]**
![Search Screen](https://github.com/Hyeonim/yemaat/assets/122865860/ab340f39-c8c3-4f24-a259-d5249d14bbd5)

- 검색한 가게의 위치 정보와 정보를 볼 수 있음
- 업태별, 위치별로 정렬하여 검색 가능

### **[공지사항]**
![Notice](https://github.com/Hyeonim/yemaat/assets/122865860/761b725e-48fa-4451-a1ea-3351ce4145ec)

- 가게 관련 공지사항을 확인 가능
- 중요한 공지사항은 상단에 고정으로 표시
- 공지사항 클릭시 상세 보기 가능

### **[이벤트]**
![Event](https://github.com/Hyeonim/yemaat/assets/122865860/6d3a575f-7523-4eaa-8fed-f2d3e2fba6f8)

- 가게 페이지에서 추가한 이벤트를 확인 가능
- 가게에서 정한 기한이 끝이나면 종료된 이벤트로 전환
- 시작 기한이 아직 아니라면 다가올 이벤트에 포함

### **[가게 상세 정보]**
![Store Detail](https://github.com/Hyeonim/yemaat/assets/122865860/2e549e13-636b-426b-a7b0-9d2b61e9ee84)

- 가게 상세 페이지로 가게에 대한 정보를 확인 가능(위치, 번호, 메뉴 등)
- 가게 이용 후 손님들이 쓴 리뷰를 확인 가능
- 리뷰가 신고를 받았을 시 블러 처리가 됨
- 가게에 대한 예약이 가능한 페이지

### **[마이 페이지]**
![My Page](https://github.com/Hyeonim/yemaat/assets/122865860/9a234086-98f6-46ba-bd94-de5c2cbba879)

- 예약한 정보와 예약했던 가게 등의 정보를 확인 가능
- 가게 사용이 완료 후 리뷰 작성이 가능
- 관리자에게 문의가 가능

### **[점주 페이지]**
![Owner Page](https://github.com/Hyeonim/yemaat/assets/122865860/307b4a63-bc19-4ea0-bac2-743aa6ef5083)

- 가게를 등록할 수 있음
- 가게에 대한 메뉴, 정보를 수정, 추가가 가능
- 가게에 대한 이벤트 추가가 가능

### **[관리자 페이지]**
![Admin Page](https://github.com/Hyeonim/yemaat/assets/122865860/6e71a544-19b0-4ab8-8984-2ad9b86cd185)

- 고객, 점주, 가게의 정보를 확인, 수정, 삭제가 가능
- 고객에 대한 신고가 많을 시 블랙리스트 처리 및 해제가 가능
- 리뷰 신고가 왔을 시 확인 후 처리가 가능
- 가게 페이지에서 가게 오픈 신청을 하였을 시 수락 및 취소가 가능
- 폐업 신청을 하였을 시 상태 변경이 가능
- 마이 페이지에서 온 문의에 대한 답변을 할 수 있음
- 공지사항을 추가, 삭제, 수정이 가능하고 상단에 고정 공지를 설정 가능

### **[로그인 페이지]**
![Login Page](https://github.com/Hyeonim/yemaat/assets/122865860/93008dad-f4a8-4022-bbc8-353f29aea916)

- 홈페이지에서 회원가입을 하거나 소셜 로그인을 통해 로그인이 가능
- 아이디, 비밀번호 찾기가 가능
