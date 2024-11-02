package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.entity.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class MenuServiceInitiateTest {

    @Autowired
    private MenuService menuService;

    @SuppressWarnings("NonAsciiCharacters")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Commit
    void initiate() {
        Menu 정보공개 = menuService.create(new CreateMenuCommand(null, "정보공개", "/", 1));
        Menu 국민참여 = menuService.create(new CreateMenuCommand(null, "국민참여", "/", 2));
        Menu 알림홍보 = menuService.create(new CreateMenuCommand(null, "알림·홍보", "/", 3));
        Menu 사업안내 = menuService.create(new CreateMenuCommand(null, "사업안내", "/", 4));
        Menu 열린경영 = menuService.create(new CreateMenuCommand(null, "열린경영", "/", 5));
        Menu 기관소개 = menuService.create(new CreateMenuCommand(null, "기관소개", "/", 6));

        Menu 정보공개$정보공개제도_안내 = menuService.create(new CreateMenuCommand(정보공개.getId(), "정보공개제도 안내", "/", 1));
        Menu 정보공개$경영공시 = menuService.create(new CreateMenuCommand(정보공개.getId(), "경영공시", "/", 2));
        Menu 정보공개$정보목록 = menuService.create(new CreateMenuCommand(정보공개.getId(), "정보목록", "/", 3));
        Menu 정보공개$사전정보공표 = menuService.create(new CreateMenuCommand(정보공개.getId(), "사전정보공표", "/", 4));
        Menu 정보공개$사업실명제 = menuService.create(new CreateMenuCommand(정보공개.getId(), "사업실명제", "/", 5));
        Menu 정보공개$공공데이터 = menuService.create(new CreateMenuCommand(정보공개.getId(), "공공데이터", "/", 6));
        Menu 정보공개$정보공개청구 = menuService.create(new CreateMenuCommand(정보공개.getId(), "정보공개청구", "/", 7));

        Menu 국민참여$국민소통 = menuService.create(new CreateMenuCommand(국민참여.getId(), "국민소통", "/", 1));
        Menu 국민참여$고객광장 = menuService.create(new CreateMenuCommand(국민참여.getId(), "고객광장", "/", 2));
        Menu 국민참여$E_클린센터 = menuService.create(new CreateMenuCommand(국민참여.getId(), "E-클린센터", "/", 3));

        Menu 알림홍보$공지사항 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "공지사항", "/", 1));
        Menu 알림홍보$보도자료 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "보도자료", "/", 2));
        Menu 알림홍보$사진뉴스 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "사진뉴스", "/", 3));
        Menu 알림홍보$입찰공고 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "입찰공고", "/", 4));
        Menu 알림홍보$협력기관 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "협력기관", "/", 5));
        Menu 알림홍보$역대수상_및_인증기록 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "역대수상 및 인증기록", "/", 6));
        Menu 알림홍보$홍보동영상 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "홍보동영상", "/", 7));
        Menu 알림홍보$웹진 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "웹진", "/", 8));
        Menu 알림홍보$자주묻는_질문 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "자주묻는 질문", "/", 9));
        Menu 알림홍보$유실물센터 = menuService.create(new CreateMenuCommand(알림홍보.getId(), "유실물센터", "/", 10));

        Menu 사업안내$올림픽스포츠센터 = menuService.create(new CreateMenuCommand(사업안내.getId(), "올림픽스포츠센터", "/", 1));
        Menu 사업안내$올림픽공원 = menuService.create(new CreateMenuCommand(사업안내.getId(), "올림픽공원", "/", 2));
        Menu 사업안내$미사경정공원 = menuService.create(new CreateMenuCommand(사업안내.getId(), "미사경정공원", "/", 3));
        Menu 사업안내$대관서비스 = menuService.create(new CreateMenuCommand(사업안내.getId(), "대관서비스", "/", 4));

        Menu 열린경영$ESG경영 = menuService.create(new CreateMenuCommand(열린경영.getId(), "ESG경영", "/", 1));
        Menu 열린경영$윤리경영 = menuService.create(new CreateMenuCommand(열린경영.getId(), "윤리경영", "/", 2));
        Menu 열린경영$인권경영 = menuService.create(new CreateMenuCommand(열린경영.getId(), "인권경영", "/", 3));
        Menu 열린경영$일자리창출 = menuService.create(new CreateMenuCommand(열린경영.getId(), "일자리창출", "/", 4));
        Menu 열린경영$사회공헌 = menuService.create(new CreateMenuCommand(열린경영.getId(), "사회공헌", "/", 5));

        Menu 기관소개$한국체육산업개발 = menuService.create(new CreateMenuCommand(기관소개.getId(), "한국체육산업개발", "/", 1));
        Menu 기관소개$인사말 = menuService.create(new CreateMenuCommand(기관소개.getId(), "인사말", "/", 2));
        Menu 기관소개$조직도 = menuService.create(new CreateMenuCommand(기관소개.getId(), "조직도", "/", 3));
        Menu 기관소개$CI소개 = menuService.create(new CreateMenuCommand(기관소개.getId(), "CI소개", "/", 4));
        Menu 기관소개$오시는길 = menuService.create(new CreateMenuCommand(기관소개.getId(), "오시는길", "/", 5));

        Menu 정보공개$정보공개제도_안내$개요 = menuService.create(new CreateMenuCommand(정보공개$정보공개제도_안내.getId(), "개요", "/", 1));
        Menu 정보공개$정보공개제도_안내$처리절차 = menuService.create(new CreateMenuCommand(정보공개$정보공개제도_안내.getId(), "처리절차", "/", 2));
        Menu 정보공개$정보공개제도_안내$공개방법 = menuService.create(new CreateMenuCommand(정보공개$정보공개제도_안내.getId(), "공개방법", "/", 3));
        Menu 정보공개$정보공개제도_안내$불복구제절차 = menuService.create(new CreateMenuCommand(정보공개$정보공개제도_안내.getId(), "불복구제절차", "/", 4));
        Menu 정보공개$정보공개제도_안내$비공개_대상_정보 = menuService.create(new CreateMenuCommand(정보공개$정보공개제도_안내.getId(), "비공개 대상 정보", "/", 5));

        Menu 정보공개$경영공시$통합공시 = menuService.create(new CreateMenuCommand(정보공개$경영공시.getId(), "통합공시", "/", 1));
        Menu 정보공개$경영공시$자체공시 = menuService.create(new CreateMenuCommand(정보공개$경영공시.getId(), "자체공시", "/", 2));
        Menu 정보공개$경영공시$경영공시_소개 = menuService.create(new CreateMenuCommand(정보공개$경영공시.getId(), "경영공시 소개", "/", 3));
        Menu 정보공개$경영공시$경영공시_담당자 = menuService.create(new CreateMenuCommand(정보공개$경영공시.getId(), "경영공시 담당자", "/", 4));
        Menu 정보공개$경영공시$자주묻는_질문 = menuService.create(new CreateMenuCommand(정보공개$경영공시.getId(), "자주묻는 질문", "/", 5));
        Menu 정보공개$경영공시$경영공시_개선센터 = menuService.create(new CreateMenuCommand(정보공개$경영공시.getId(), "경영공시 개선센터", "/", 6));

        Menu 정보공개$공공데이터$공공데이터_이용안내 = menuService.create(new CreateMenuCommand(정보공개$공공데이터.getId(), "공공데이터 이용안내", "/", 1));
        Menu 정보공개$공공데이터$공공데이터_활용안내 = menuService.create(new CreateMenuCommand(정보공개$공공데이터.getId(), "공공데이터 활용안내", "/", 2));
        Menu 정보공개$공공데이터$공공데이터_개방목록 = menuService.create(new CreateMenuCommand(정보공개$공공데이터.getId(), "공공데이터 개방목록", "/", 3));
        Menu 정보공개$공공데이터$공공데이터_의견제안 = menuService.create(new CreateMenuCommand(정보공개$공공데이터.getId(), "공공데이터 의견제안", "/", 4));

        Menu 국민참여$국민소통$정책제안 = menuService.create(new CreateMenuCommand(국민참여$국민소통.getId(), "정책제안", "/", 1));
        Menu 국민참여$국민소통$설문참여 = menuService.create(new CreateMenuCommand(국민참여$국민소통.getId(), "설문참여", "/", 2));
        Menu 국민참여$국민소통$공모참여 = menuService.create(new CreateMenuCommand(국민참여$국민소통.getId(), "공모참여", "/", 3));
        Menu 국민참여$국민소통$국민심사 = menuService.create(new CreateMenuCommand(국민참여$국민소통.getId(), "국민심사", "/", 4));
        Menu 국민참여$국민소통$프로그램운영제안 = menuService.create(new CreateMenuCommand(국민참여$국민소통.getId(), "프로그램운영제안", "/", 5));

        Menu 국민참여$고객광장$고객서비스헌장 = menuService.create(new CreateMenuCommand(국민참여$고객광장.getId(), "고객서비스헌장", "/", 1));
        Menu 국민참여$고객광장$전자민원 = menuService.create(new CreateMenuCommand(국민참여$고객광장.getId(), "전자민원", "/", 2));
        Menu 국민참여$고객광장$고객의_소리 = menuService.create(new CreateMenuCommand(국민참여$고객광장.getId(), "고객의 소리", "/", 3));
        Menu 국민참여$고객광장$고객_모니터링단 = menuService.create(new CreateMenuCommand(국민참여$고객광장.getId(), "고객 모니터링단", "/", 4));

        Menu 국민참여$E_클린센터$부패익명신고 = menuService.create(new CreateMenuCommand(국민참여$E_클린센터.getId(), "부패익명신고", "/", 1));
        Menu 국민참여$E_클린센터$공익신고 = menuService.create(new CreateMenuCommand(국민참여$E_클린센터.getId(), "공익신고", "/", 2));
        Menu 국민참여$E_클린센터$청탁금지법_위반신고 = menuService.create(new CreateMenuCommand(국민참여$E_클린센터.getId(), "청탁금지법 위반신고", "/", 3));
        Menu 국민참여$E_클린센터$이해충돌방지법_의무_위반신고 = menuService.create(new CreateMenuCommand(국민참여$E_클린센터.getId(), "이해충돌방지법 의무·위반신고", "/", 4));
        Menu 국민참여$E_클린센터$클린신고 = menuService.create(new CreateMenuCommand(국민참여$E_클린센터.getId(), "클린신고", "/", 5));

        Menu 알림홍보$웹진$경영백서 = menuService.create(new CreateMenuCommand(알림홍보$웹진.getId(), "경영백서", "/", 1));
        Menu 알림홍보$웹진$체육산업TALK = menuService.create(new CreateMenuCommand(알림홍보$웹진.getId(), "체육산업TALK", "/", 2));

        Menu 사업안내$올림픽스포츠센터$올림픽수영장 = menuService.create(new CreateMenuCommand(사업안내$올림픽스포츠센터.getId(), "올림픽수영장", "/", 1));
        Menu 사업안내$올림픽스포츠센터$올림픽공원스포츠센터 = menuService.create(new CreateMenuCommand(사업안내$올림픽스포츠센터.getId(), "올림픽공원스포츠센터", "/", 2));
        Menu 사업안내$올림픽스포츠센터$올림픽테니스장 = menuService.create(new CreateMenuCommand(사업안내$올림픽스포츠센터.getId(), "올림픽테니스장", "/", 3));
        Menu 사업안내$올림픽스포츠센터$올팍축구장 = menuService.create(new CreateMenuCommand(사업안내$올림픽스포츠센터.getId(), "올팍축구장", "/", 4));
        Menu 사업안내$올림픽스포츠센터$일산올림픽스포츠센터 = menuService.create(new CreateMenuCommand(사업안내$올림픽스포츠센터.getId(), "일산올림픽스포츠센터", "/", 5));
        Menu 사업안내$올림픽스포츠센터$분당올림픽스포츠센터 = menuService.create(new CreateMenuCommand(사업안내$올림픽스포츠센터.getId(), "분당올림픽스포츠센터", "/", 6));
        Menu 사업안내$올림픽스포츠센터$온라인수강_및_예약 = menuService.create(new CreateMenuCommand(사업안내$올림픽스포츠센터.getId(), "온라인수강 및 예약", "/", 7));

        Menu 사업안내$대관서비스$대관절차_안내 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "대관절차 안내", "/", 1));
        Menu 사업안내$대관서비스$대관운영_규정 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "대관운영 규정", "/", 2));
        Menu 사업안내$대관서비스$대관운영_시행세칙 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "대관운영 시행세칙", "/", 3));
        Menu 사업안내$대관서비스$대관요율표 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "대관요율표", "/", 4));
        Menu 사업안내$대관서비스$대관_시설자료 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "대관&시설자료", "/", 5));
        Menu 사업안내$대관서비스$올림픽공원대관시설 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "올림픽공원대관시설", "/", 6));
        Menu 사업안내$대관서비스$올림픽공원_대관예약 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "올림픽공원 대관예약", "/", 7));
        Menu 사업안내$대관서비스$미사경정공원대관시설 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "미사경정공원대관시설", "/", 8));
        Menu 사업안내$대관서비스$미사경정공원_대관예약 = menuService.create(new CreateMenuCommand(사업안내$대관서비스.getId(), "미사경정공원 대관예약", "/", 9));

        Menu 열린경영$윤리경영$추진방향 = menuService.create(new CreateMenuCommand(열린경영$윤리경영.getId(), "추진방향", "/", 1));
        Menu 열린경영$윤리경영$윤리규범 = menuService.create(new CreateMenuCommand(열린경영$윤리경영.getId(), "윤리규범", "/", 2));
        Menu 열린경영$윤리경영$윤리경영활동 = menuService.create(new CreateMenuCommand(열린경영$윤리경영.getId(), "윤리경영활동", "/", 3));
        Menu 열린경영$윤리경영$감사결과_공개 = menuService.create(new CreateMenuCommand(열린경영$윤리경영.getId(), "감사결과 공개", "/", 4));

        Menu 열린경영$인권경영$인권경영_선언문 = menuService.create(new CreateMenuCommand(열린경영$인권경영.getId(), "인권경영 선언문", "/", 1));
        Menu 열린경영$인권경영$인권경영_운영지침 = menuService.create(new CreateMenuCommand(열린경영$인권경영.getId(), "인권경영 운영지침", "/", 2));
        Menu 열린경영$인권경영$인권경영_소식 = menuService.create(new CreateMenuCommand(열린경영$인권경영.getId(), "인권경영 소식", "/", 3));
        Menu 열린경영$인권경영$인권침해신고센터 = menuService.create(new CreateMenuCommand(열린경영$인권경영.getId(), "인권침해신고센터", "/", 4));

        Menu 기관소개$한국체육산업개발$기관개요 = menuService.create(new CreateMenuCommand(기관소개$한국체육산업개발.getId(), "기관개요", "/", 1));
        Menu 기관소개$한국체육산업개발$비전 = menuService.create(new CreateMenuCommand(기관소개$한국체육산업개발.getId(), "비전", "/", 2));
        Menu 기관소개$한국체육산업개발$연혁 = menuService.create(new CreateMenuCommand(기관소개$한국체육산업개발.getId(), "연혁", "/", 3));

        Menu 기관소개$인사말$대표이사_인사말 = menuService.create(new CreateMenuCommand(기관소개$인사말.getId(), "대표이사 인사말", "/", 1));
        Menu 기관소개$인사말$대표이사_프로필 = menuService.create(new CreateMenuCommand(기관소개$인사말.getId(), "대표이사 프로필", "/", 2));

        Menu 기관소개$CI소개$심볼마크_시그니처 = menuService.create(new CreateMenuCommand(기관소개$CI소개.getId(), "심볼마크, 시그니처", "/", 1));
        Menu 기관소개$CI소개$색상규정 = menuService.create(new CreateMenuCommand(기관소개$CI소개.getId(), "색상규정", "/", 2));
    }

}
