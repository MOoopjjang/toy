# 개발진행 및 Bug  


## todo  
- 회원가입 등록 / 편집 기능 ( O )
- todo 목록 삭제 기능 ( O ) 
- 이미지 upload 기능 구현 ( O )
- logout ( O )
- token 정보 localStorage에 정보저장 ( O )
- 환경변수 설정 
- 코드 refactoring ( 진행중 )
  *  axios 모듈화  
     * https://pids.tistory.com/249  

- filter > "초기화" 기능 구현
     



## bug  
- main > thumbnail 클릭 > dropdown menu > profile 클릭 > dropdown menu가 펼쳐진채 유지됨 ( O )
- thumbnail ( X )시 default 이미지 노출
- todo목록 > checkbox 클릭 > checkbox 체크 X, 상세보기로 이동 > checkbox영역 클릭시 체크 O  ( O )
- todo선택 > 삭제 > 삭제팝업 여러번 뜨며 , 오류메세지 팝업 출력 , 데이타는 삭제되었음 ( db 확인 ) ( O )
- token (x)->메인페이지->메인페이지 화면 나온후 로그인페이지로 이동됨 -> 메인페이지화면이 나오면 안됨
- 메인화면 > top > 날짜  : 오늘 날짜로 셋팅 ( O )