### JAR 다운로드 링크 : https://drive.google.com/drive/u/0/folders/1LfOvU-bmkU1eSmoVbu4NouMBCAc768vk

<hr/>

## API-1 [POST] 블로그 목록 조회 <br>url : localhost:8090/search/blog<br>sortType : 0 => 정확도순, 1=> 최신순
### 요청 예시: {
    "query": "keyword2",
    "sortType": 0,
    "page": 2,
    "pageSize": 5
}

### 응답 예시 {
    "sortType": "ACCURACY",
    "searchDomainType": "KAKAO",
    "totalCount": 797,
    "totalPage": 160,
    "requestedPage": 2,
    "blogInfos": [
        {
            "domainType": "KAKAO",
            "blogName": "프젝 및 강의 내용 복습",
            "thumbnail": "https://search3.kakaocdn.net/argon/130x130_85_c/2RdaCBGi2Tn",
            "url": "http://ham-s.tistory.com/69",
            "title": "구현 03 Kakao Maps API <b>Keyword</b> 검색",
            "contents": "1. Document Link - <b>Keyword</b> Search <b>2</b>. 분석 1) Maps API <b>Keyword</b> 검색 Rest API DataSet 분석 (1) 요청 변수 (<b>2</b>) 응답 변수 (3) 분석 - 검색은 <b>Keyword</b>로 하지만 서버 저장 후 Map에 표시 하기 위해서는 변수 선택 필요. - 좌표, Place Name, ID 저장 3. 구현 1) 검색 &lt;!DOCTYPE HTML&gt; &lt;html&gt; &lt;head&gt; &lt;meta charset...",
            "datetime": null
        },
        {
            "domainType": "KAKAO",
            "blogName": "cosmog",
            "thumbnail": "https://search4.kakaocdn.net/argon/130x130_85_c/3JWokacLpLK",
            "url": "http://cosmog.tistory.com/115",
            "title": "[JAVA] 16-<b>2</b>. singleton Design Pattern &amp; final <b>Keyword</b> &amp; 상수(constant) &amp; abstract",
            "contents": "Singleton(); //<b>2</b>. 객체 생성을 못하도록 생성자를 private처리 private Singleton() {} //3. s변수(나자신 객체)를 getter로 반환 // static 키워드를 붙임 public static Singleton getInstance() { return instance; } public String site = &#34;aaa&#34;; } 🔆Main class main에서 객체를 몇번이고 생성해도 singleton class...",
            "datetime": null
        },
        {
            "domainType": "KAKAO",
            "blogName": "하루노트",
            "thumbnail": "",
            "url": "http://inoino9.tistory.com/27",
            "title": "<b>Keyword</b> Field를 활용하는 다양한 쿼리 예제",
            "contents": "문서를 찾으려면 다음과 같이 사용할 수 있습니다. GET http://localhost:9200/search_test_index/_search { &#34;query&#34;: { &#34;term&#34;: { &#34;<b>keyword</b>_field&#34;: { &#34;value&#34;: &#34;Elasticsearch&#34; } } } } <b>2</b>. terms query terms query는 주어진 terms 중 하나라도 매칭되는 모든 문서를 찾습니다. 예를 들어, &#34;test1&#34; 또는...",
            "datetime": null
        },
        {
            "domainType": "KAKAO",
            "blogName": "글로벌 음원 유통사 Sound Republica",
            "thumbnail": "https://search1.kakaocdn.net/argon/130x130_85_c/36jf8UyNuuP",
            "url": "https://blog.naver.com/soundrepublica/223019119758",
            "title": "[Weekly Best] 이주의 발견, HOT #<b>Keyword</b> - <b>2</b>월 3주차",
            "contents": "한 주의 주목해야 할 Music <b>Keyword</b>! &lt;이주의 발견, HOT #<b>Keyword</b>&gt; <b>2</b>월 3주차 #tripleS (트리플에스) - Rising ​ ​ ASSEMBLE ​ ARTIST tripleS (트리플에스) ALBUM EP RELEASE DATE 2023. 02. 13 GENRE Dance 혁신적인 아이돌, 트리플에스가 10인의 베일을 벗었습니다. ​ 타이틀곡 &#39;Rising&#39;은 꿈을 향해 달려가는 소녀들에...",
            "datetime": null
        },
        {
            "domainType": "KAKAO",
            "blogName": "대디베어의 지식탐구생활",
            "thumbnail": "https://search3.kakaocdn.net/argon/130x130_85_c/FYfEy253ShE",
            "url": "https://blog.naver.com/daddy-bear-life/223041870137",
            "title": "ChatGPT와 SEMrush로 구글 SEO 최적화 - 키워드 조사 (<b>Keyword</b> Research)",
            "contents": "SF 점수는 0에서 10까지의 범위를 갖으며, 높은 점수는 해당 키워드를 검색 결과 페이지 상위에 노출시키는 것이 더욱 어려운 경쟁 상황을 의미합니다. ​ <b>2</b>) KD 값 (<b>Keyword</b> Difficulty) 해당 키워드를 순위에 노출시키기 위해 경쟁 업체들이 얼마나 노력해야 하는지를 나타내는 수치입니다. 이 값은 0~100 사이의 범위를...",
            "datetime": null
        }
    ],
    "eol": false
}

<hr/>

## API-2 [GET] hot 10 검색어 조회 <br>url : localhost:8090/search/blog
### 응답 예시 : {
    "searchCountInfos": [
        {
            "keyword": "keyword2",
            "count": 4
        }
    ]
}

<hr/>

### 1. 카카오 장애시 폴백 기능 구현은, Retry를 이용하여 네이버 쪽으로 요청하도록 구현하였습니다.
### 2. 대용량 트래픽 및을 위하여 db단에서 병목현상을 최소화 하도록 Weblfux 및 로컬 캐시로 구현.
### 3. 대규모 조회수 update 동시성에 관한 부분은, 로컬캐시를 사용하여 주기적으로 업데이트 되도록 구현.