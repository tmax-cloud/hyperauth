## Tmax Keycloak Theme

### 주의사항

- tmax theme 및 타 client theme 변경 금지
- theme명 branch에서 작업 후, dev에 merge(ex. hypercloud)

#### 개발 환경 구축 (Keycloak on OpenJDK)

1. Download Keycloak (https://www.keycloak.org/getting-started/getting-started-zip) (keycloak 버전 : 11.0.2 사용!!)
2. themes/ 폴더에 git repository 연결
3. standalone/configuration/standalone.xml 파일의 <theme> 엘리먼트 수정.

```
<theme>
    <!-- cache 설정 변경: 저장 시 화면에 바로 반영 -->
    <staticMaxAge>-1</staticMaxAge>
    <cacheThemes>false</cacheThemes>
    <cacheTemplates>false</cacheTemplates>
    <dir>\${jboss.home.dir}/themes</dir>
    <welcomeTheme>tmax</welcomeTheme>
</theme>
```

4. 실행 bin/standalone.bat

#### 새로운 Custom Theme 추가

1. theme이름으로 폴더 추가
2. 기존재하는 base/tmax theme를 parent로 받거나 복사하여 새로운 테마 작성
  - 기본은 template.ftl(페이지 Layout)에 각 페이지 별로 추가 부분 nested로 구현되어 있으나 각 theme 별로 자유롭게 변경 가능

- 테마 작성 완료 후
1. dev branch에 merge
2. 해당 theme 폴더가 배포 시 추가되도록 포함 요청(CK1-3팀: 추가한 theme 폴더 이름 전달)