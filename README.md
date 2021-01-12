# WORD MASTER

## 한양대학교 컴퓨터소프트웨어학부 양동해, 카이스트 전기및전자공학부 우혜인

본 어플은 서버를 활용하는 애플리케이션으로서 총 3개의 탭으로 구성되어 있으며, 시작하기 전 페이스북 계정을 통해 로그인할 수 있습니다.
주로 사용된 기능은 Node js, Mongo DB, Facebook SDK, Retrofit, Wiktionary API 입니다.

### 0) 시작 화면
기기에서 앱을 처음으로 시작할 때는 카메라, 연락처, 내부 저장소, 전화 권한을 요청합니다. 이 4개의 항목에 모두 동의해야 앱을 이용할 수 있습니다. 동의를 모두 마치면 페이스북 로그인 창이 뜹니다.
이는 페이스북 SDK를 이용한 것으로서, 이용자의 이름과 개별ID를 수집합니다.
페이스북 계정이 있다면 로그인 성공 후에 'START APP!' 버튼을 눌러 앱을 시작합니다. 
로그인하지 않은 채 'START APP!' 버튼을 눌러도 앱을 이용할 수는 있으나, 서버와 통신 시 customized된 기능을 이용할 수 없습니다.

### 1) Contacts 탭
처음 시작할 때 기기의 연락처 데이터를 받아옵니다. 기기와의 동기화는 앱 실행 후 최초 1회 이후 이루어지지 않습니다. 
연락처 리스트의 오른쪽 전화 버튼을 누르면 기기에서 바로 전화를 걸 수 있습니다. 길게 누르면 연락처 삭제가 가능하며, 검색 기능도 지원합니다.
'연락처 추가' 버튼을 누르면 이름과 전화번호, 사진을 입력하여 새로운 연락처를 등록할 수 있습니다. 이는 앱 상에서만 추가가 가능하며 기기와 동기화되지 않습니다.
'백업' 버튼을 누르면 현재 앱에 떠 있는 연락처 리스트를 서버에 업데이트합니다. 
개별 페이스북 계정으로 로그인했다면 그 계정과 연결된 별도의 DB에 저장되며, 그렇지 않았다면 서버에 있는 앱의 기본 DB에 저장됩니다. 
'복구' 버튼을 누르면 DB에 저장되어 있는 연락처를 앱에 띄워 보여줍니다. 겹치는 연락처는 제외하고 없는 연락처만 업데이트합니다.

### 2) Gallery 탭
마찬가지로 처음 시작할 때 기기의 사진을 받아오며, 동기화는 앱 실행 후 최초 1회 이후에는 이루어지지 않습니다.
사진을 길게 누르면 삭제할 수 있으며, 한 번 클릭하면 사진을 크게 띄워 줌인/줌아웃할 수 있습니다. 이 상태에서 다시 길게 누르면 서버에 사진을 업로드하게 됩니다.
'카메라' 버튼을 누르면 바로 카메라가 실행되어 사진을 찍고 앱에 저장할 수 있습니다.
'백업된 이미지' 버튼을 누르면 서버에 업로드한 사진을 볼 수 있습니다. 마찬가지로 길게 누르면 (서버에서) 삭제할 수 있으며, 한 번 클릭하면 사진을 크게 띄울 수 있습니다.
'복구' 버튼을 누르면 앞서 '백업된 이미지'에서 본 서버에 업로드되어 있는 사진을 현재 탭으로 업데이트하게 됩니다.

### 3) WORD GAME 탭
영어로 끝말잇기 게임입니다. 게임을 시작하려면 START 버튼을 누르고, 어떤 캐릭터와 게임을 할지 고릅니다. 고르고 나면 START 버튼은 VERIFY 버튼으로 바뀝니다.
단어를 입력한 후 VERIFY를 누르면 그 단어의 유효성을 검증할 수 있습니다. 만약 VERIFY가 SUBMIT으로 바뀐다면 유효한 단어입니다. 
이는 Wiktionary API를 통해 위키백과에 존재하는 단어인지 확인하는 식으로 이루어집니다.
이후 SUBMIT을 누르면, 하단의 로그 창에 컴퓨터가 보내는 단어가 보여집니다. 이 과정은 사용자가 입력한 단어를 DB에 보내고, DB에서는 이를 바탕으로 적절한 단어를 보내는 방식으로 작동합니다.
게임은 You win! 또는 You Lose! 가 뜰 때까지 계속됩니다.

- 짱구-회사원-알파고 순으로 난이도가 어려워집니다.
- 난이도별로 다른 DB를 운영하며, 어려운 난이도일수록 DB에 저장된 단어 수가 많습니다.
- 우측 하단 버튼을 누르면 지금까지 게임을 한 사용자들의 점수표를 볼 수 있습니다. 서버별로 별개의 점수표가 운영됩니다.
- 이전에 나왔던 단어, 이전 단어의 마지막 글자로 시작하지 않는 단어를 입력한 경우에는 사용자가 집니다.
- 컴퓨터 DB에 더이상 적절한 단어가 없을 경우에는 사용자가 이깁니다.
- 점수 기록은 사용자가 이긴 경우에만, 사용자가 입력한 단어의 개수로 매겨집니다.

### 4) 기타 사항 & Bugs
만약 실행되고 있는 앱을 백그라운드로 보내면, 현재 로그인되어 있는 계정은 로그아웃되며 끝말잇기 게임 또한 초기화됩니다.

### 다운로드 방법
apk 
