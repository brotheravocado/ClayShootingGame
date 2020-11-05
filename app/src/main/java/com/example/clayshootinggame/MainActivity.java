package com.example.clayshootinggame;

import android.animation.Animator; // 애니메이션의 시작,종료, 그리고 AnimatorListeners를 추가하는 애니메션을 지원하는 클래스들의 슈퍼 클래스
import android.animation.AnimatorSet; // 여러 애니메이션을 하나의 Animator 객체 세트로 만드는 클래스
import android.animation.ObjectAnimator; // 뷰의 translation 값을 변화시키는 애니메이션
import android.animation.ValueAnimator; // 일정 시간 동안 값이 변화하는 것을 체크할 때 이용
import android.graphics.Color;
import android.graphics.Point; // 두 정수 좌표 값을 갖는 점 제공
import androidx.constraintlayout.widget.ConstraintLayout;// 반응형 UI 레이아웃
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display; // 화면의 크기와 밀도에 관한 정보를 제공
import android.view.View; // 뷰를 클릭할 때 호출할 콜백의 인터페이스 정의
import android.view.ViewGroup;
import android.widget.ImageView; // 화면에 이미지 파일 출력
import android.widget.RelativeLayout;
import android.widget.TextView; // text를 뷰형태로 보여줌
import android.widget.Toast; // 짧은 메시지


public class MainActivity extends AppCompatActivity implements View.OnClickListener { //  사용자 클릭 이벤트
    ImageView iv_gun; // 총 이미지 뷰
    ImageView iv_bullet; // 총알 이미지 뷰
    ImageView iv_clay; // 클레잉 이미지 뷰
    // 추가 -----------
    ImageView iv_start; // 게임시작 버튼 이미지 뷰
    ImageView iv_redclay; // 빨간클레잉 이미지 뷰
    // ---------------
    double screen_width, screen_height; // 화면 가로,세로 크기
    float bullet_height, bullet_width; // 총알 가로, 세로 크기
    float gun_height, gun_width; // 총 가로, 세로 크기
    float clay_height, clay_width; // 클레이 가로, 세로 크기
    float bullet_center_x, bullet_center_y; // 총알의 중심 좌표 (x,y)
    float clay_center_x, clay_center_y; // 클레이의 중심 좌표 (x,y)
    double gun_x, gun_y; // 총의 좌표
    double gun_center_x; // 총의 중심 좌표
    int NO_OF_CLAYS = 10; // 클레이 개당 개수

    // 추가---------------------------------
    int no_of_clays_left = NO_OF_CLAYS; // 남은 클레이 수
    int hit = 0; // 맞췄을 경우
    int red_hit = 0; // 명중한 빨간클레이 개수
    int hitclay = 0; // 명중한 초록클레이 개수
    TextView status; // 현재 맞춘 클레이 갯수를 보여주는 textview
    int total_b = 30; // 총알 개수
    int remain_b = 30; // 남은 총알 개수
    float start_height, start_width; // 시작버튼 가로, 세로 크기
    double start_x, start_y; // 시작버튼의 좌표
    double start_center_x; // 시작버튼의 중심 좌표
    boolean first = false; // 시작버튼 마우스 인식
    float redclay_height, redclay_width; // 빨강 클레이 가로, 세로 크기
    float red_clay_center_x, red_clay_center_y; // 빨간클레이의 중심 좌표 (x,y)
    int score = 0; // 점수 계산


    ObjectAnimator clay_translateX ; // 초록클레이 애니메이션
    ObjectAnimator clay_translateY ;
    ObjectAnimator clay_rotation ;

    ObjectAnimator redclay_translateX ; // 빨간클레이 애니메이션
    ObjectAnimator redclay_translateY ;
    ObjectAnimator redclay_rotation ;
    // ----------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout); // 레이아웃 인식

        Display display = getWindowManager().getDefaultDisplay();
        // display 객체 생성, 커스텀 윈도우를 보여주기 위한 윈도우 매니저를 반환
        Point size = new Point(); // size 객체 생성
        display.getSize(size); // 디스플레이의 크기를 얻음
        screen_width = size.x; // 화면의 가로 크기
        screen_height = size.y; // 화면의 세로 크기

        iv_bullet = new ImageView(this); // 애니메이션 이미지 인식
        iv_gun    = new ImageView(this);
        iv_clay   = new ImageView(this);


        // 추가 ------------------------------
        iv_redclay   = new ImageView(this); // 빨간 클레이 애니메이션 이미지 인식
        iv_start = new ImageView(this); // 시작버튼 애니메이션 이미지 인식
        status = new TextView(this); // 텍스트뷰 위치 설정
        status.setX(50f); // 텍스트뷰 x좌표 설정
        status.setY((float)screen_height - 500f); // 텍스트뷰 y좌표 설정
        status.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        status.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        status.setTextColor(Color.parseColor("#FFFFFF")); // textcolor 흰색으로 지정
        status.setTextSize(16); // 폰트 사이즈 16지정
        layout.addView(status); // view 추가

        iv_start.setImageResource(R.drawable.start); // start을 drawable로 설정 -> iv_start
        iv_start.measure(iv_start.getMeasuredWidth(), iv_start.getMeasuredHeight()); // iv_start의 뷰의 높이,너비,크기를 측정
        start_height = iv_start.getMeasuredHeight(); // 측정한 뷰의 높이를 start_height에 대입
        start_width = iv_start.getMeasuredWidth(); // 측정한 뷰의 너비를 start_width에 대입
        layout.addView(iv_start); // iv_start 뷰 추가
        // -----------------------------------

        iv_gun.setImageResource(R.drawable.gun); // gun을 drawable로 설정 -> iv_gun
        iv_gun.measure(iv_gun.getMeasuredWidth(), iv_gun.getMeasuredHeight()); // iv_gun의 뷰의 높이,너비,크기를 측정
        gun_height = iv_gun.getMeasuredHeight(); // 측정한 뷰의 높이를 gun_height에 대입
        gun_width = iv_gun.getMeasuredWidth(); // 측정한 뷰의 너비를 gun_width에 대입
        layout.addView(iv_gun); // iv_gun 뷰 추가

        iv_bullet.setImageResource(R.drawable.bullet); // bullet을 drawable로 설정 -> iv_bullet
        iv_bullet.measure(iv_bullet.getMeasuredWidth(), iv_bullet.getMeasuredHeight()); // iv_bullet의 뷰의 높이,너비,크기를 측정
        bullet_height = iv_bullet.getMeasuredHeight(); // 측정한 뷰의 높이를 bullet_height에 대입
        bullet_width  = iv_bullet.getMeasuredWidth(); // 측정한 뷰의 너비를 bullet_height에 대입
        iv_bullet.setVisibility(View.INVISIBLE); // 뷰가 화면에 안보이게 설정
        layout.addView(iv_bullet); // iv_bullet 뷰 추가

        iv_clay.setImageResource(R.drawable.greenclay); // clay를 drawable로 설정 -> iv_clay // 수정
        iv_clay.setScaleX(0.8f); // 뷰의 가로 크기 0.8f 확대/축소
        iv_clay.setScaleY(0.8f);  // 뷰의 세로 크기 0.8f 확대/축소
        iv_clay.measure(iv_bullet.getMeasuredWidth(), iv_bullet.getMeasuredHeight()); // iv_bullet의 너비,높이,크기를 측정
        clay_height = iv_clay.getMeasuredHeight(); // 측정한 뷰의 높이를 clay_height에 대입
        clay_width  = iv_clay.getMeasuredWidth();// 측정한 뷰의 너비를 clay_width에 대입
        iv_clay.setVisibility(View.INVISIBLE);   // 뷰가 화면에 안보이게 설정
        layout.addView(iv_clay); // iv_clay 뷰 추가

        gun_center_x = screen_width * 0.8; // gun의 가로 위치를 화면의 너비의 0.7 위치쯤에 있도록한다.
        // 수정 게임 설명 때문에 오른쪽으로 위치 옮김
        gun_x = gun_center_x - gun_width * 0.5; // gun의 x는 중심부로 부터 너비의 0.5크기가 왼쪽으로 이동된 위치이다.
        gun_y = screen_height - gun_height; // gun의 y는 화면의 높이에서 gun의 높이 값을 뺀 위치이다.
        iv_gun.setX((float)gun_x); // iv_gun의 위치를 설정한다.
        iv_gun.setY((float)gun_y - 100f);

        //추가 -------------
        start_center_x = screen_width * 0.5; // start의 가로 위치를 화면의 너비의 0.5 위치쯤에 있도록한다.
        // 수정 게임 설명 때문에 오른쪽으로 위치 옮김
        start_x = start_center_x - start_width * 0.5; // start의 x는 중심부로 부터 너비의 0.5크기가 왼쪽으로 이동된 위치이다.
        start_y = screen_height * 0.3; // start의 y는 화면의 높이의 반정도이다.
        iv_start.setX((float)start_x); // iv_start의 위치를 설정한다.
        iv_start.setY((float)start_y );

        iv_redclay.setImageResource(R.drawable.redclay); // redclay를 drawable로 설정 -> iv_redclay // 수정
        iv_redclay.setScaleX(0.8f); // 뷰의 가로 크기 0.8f 확대/축소
        iv_redclay.setScaleY(0.8f);  // 뷰의 세로 크기 0.8f 확대/축소
        iv_redclay.measure(iv_bullet.getMeasuredWidth(), iv_bullet.getMeasuredHeight()); // iv_bullet의 너비,높이,크기를 측정
        redclay_height = iv_redclay.getMeasuredHeight(); // 측정한 뷰의 높이를 redclay_height에 대입
        redclay_width  = iv_redclay.getMeasuredWidth();// 측정한 뷰의 너비를 redclay_width에 대입
        iv_redclay.setVisibility(View.INVISIBLE);   // 뷰가 화면에 안보이게 설정
        layout.addView(iv_redclay); // iv_redclay 뷰 추가

        redclay_translateX = ObjectAnimator.ofFloat(iv_redclay, "translationX", -200f, (float)screen_width + 100f); // redclay_translateX 애니메이션 생성
        redclay_translateY = ObjectAnimator.ofFloat(iv_redclay, "translationY", 50f, 50f); // redclay_translateY 애니메이션 생성
        redclay_rotation   = ObjectAnimator.ofFloat(iv_redclay, "rotation", 0f, 1080f); // redclay_rotation 애니메이션 생성

        redclay_translateX.setRepeatCount(NO_OF_CLAYS-1); // 클레이 수 만큼 x 이동
        redclay_translateY.setRepeatCount(NO_OF_CLAYS-1); // 클레이 수 만큼 y 이동
        redclay_rotation.setRepeatCount(NO_OF_CLAYS-1);// 클레이 수 만큼 회전 이동
        redclay_translateX.setDuration(2500); // redclay_translateX을 2500 동안 실행
        redclay_translateY.setDuration(2500); // redclay_translateY을 2500 동안 실행
        redclay_rotation.setDuration(2500); // redclay_rotation을 2500 동안 실행

        redclay_translateX.addListener(new Animator.AnimatorListener() { // 빨간 클레이 애니메이션 리스너
            @Override
            public void onAnimationStart(Animator animator) { // 애니메이션 시작

                iv_redclay.setVisibility(View.VISIBLE); // 화면에 클레이 뷰 INVISIBLE -> VISIBLE  변환

            }

            @Override
            public void onAnimationEnd(Animator animator) { // 애니메이션이 종료될 때 실행

                if(red_hit == 1){ // 빨간 클레이를 맞춘경우
                    score += 15; // 점수 15점 추가
                    hitclay ++; // 맞춘 클레이 횟수 증가
                }
                red_hit = 0;
            }

            @Override
            public void onAnimationCancel(Animator animator) { // 애니메이션이 취소될 때 실행

            }

            @Override
            public void onAnimationRepeat(Animator animator) { // 애니메이션 반복 시

                if(red_hit == 1){ // 빨간 클레이를 맞춘경우
                    score += 15; // 15점 추가
                    hitclay ++; // 맞춘 클레이 횟수 증가
                }
                red_hit = 0; // 초기화

                if(no_of_clays_left > 0){ // 클레이 개수가 0이상인 경우
                    iv_redclay.setVisibility(View.VISIBLE); // 클레이 뷰를 VISIBLE로 설정
                }


            }

        });
        // -----------------


        clay_translateX = ObjectAnimator.ofFloat(iv_clay, "translationX", -200f, (float)screen_width + 100f); // clay_translateX 애니메이션 생성
        clay_translateY = ObjectAnimator.ofFloat(iv_clay, "translationY", 50f, 50f); // clay_translateY 애니메이션 생성
        clay_rotation   = ObjectAnimator.ofFloat(iv_clay, "rotation", 0f, 1080f); // clay_rotation 애니메이션 생성

        clay_translateX.setRepeatCount(NO_OF_CLAYS-1); // 클레이 수 만큼 x 이동
        clay_translateY.setRepeatCount(NO_OF_CLAYS-1); // 클레이 수 만큼 y 이동
        clay_rotation.setRepeatCount(NO_OF_CLAYS-1);// 클레이 수 만큼 회전
        clay_translateX.setDuration(4000); // clay_translateX을 4000 동안 실행
        clay_translateY.setDuration(4000); // clay_translateY을 4000 동안 실행
        clay_rotation.setDuration(4000); // clay_rotation을 4000 동안 실행

        clay_translateX.addListener(new Animator.AnimatorListener() { // 애니메이션 리스너
            @Override
            public void onAnimationStart(Animator animator) { // 애니메이션 시작
                iv_clay.setVisibility(View.VISIBLE); // 화면에 클레이 뷰 INVISIBLE -> VISIBLE  변환

                // 추가 -------------------
                status.setText("게임 시작" + "\n명중한 수:" + hitclay +"\n총점: " + score+ " \n 남은 총알 수: " + remain_b +"/" + total_b); // 현재 상태 출력
                // ------------------------

            }

            @Override
            public void onAnimationEnd(Animator animator) { // 애니메이션이 종료될 때 실행
                // 추가 -----------------
                if(hit == 1){ // 초록 클레이를 맞춘경우
                    hitclay ++; // 맞춘 횟수 증가
                    score += 5; // 점수 5점 추가
                }
                hit = 0;
                no_of_clays_left --; // 클레이 횟수 감소
                iv_gun.setClickable(false); // iv_gun 클릭 이벤트 비활성화
                status.setText("게임 종료!!" + "\n명중한 수:" + hitclay +"\n총점: " + score+ " \n 남은 총알 수: " + remain_b +"/" + total_b); // 현재 상태 출력
                // -------------------
                //Toast.makeText(getApplicationContext(), "게임 종료!!", Toast.LENGTH_SHORT).show(); // 게임 종료 시 게임 종료 출력
            }

            @Override
            public void onAnimationCancel(Animator animator) { // 애니메이션이 취소될 때 실행

            }

            @Override
            public void onAnimationRepeat(Animator animator) { // 애니메이션 반복 시

                if(hit == 1){ // 초록클레이를 맞춘경우
                    score += 5; // 점수 5점 추가
                    hitclay ++; // 맞춘 클레이 횟수 증가
                }
                hit = 0;
                no_of_clays_left --; // 클레이 개수 감소
                if(no_of_clays_left > 0){ // 만약 클레이가 0보다 크다면 진행
                    iv_clay.setVisibility(View.VISIBLE); // 클레이 뷰를 VISIBLE로 설정
                    status.setText("게임 중"  + "\n명중한 수:" + hitclay +"\n총점: " + score+ " \n 남은 총알 수: " + remain_b +"/" + total_b); // 현재 상태 출력
                    // ---------------------------------------------------------------------
                }


            }

        });
        Toast.makeText(getApplicationContext(), "게임 규칙!!\n 총알은 30발\n 초록 클레이 + 5점 빨간클레이 + 15점 \n 총 20개로 200점 만점!!", Toast.LENGTH_LONG).show();
        // 게임 규칙 설명


        // 추가 -------------
        iv_start.setClickable(true); // iv_start 클릭 이벤트 활성화
        iv_start.setOnClickListener(this); // iv_start 뷰 클릭될 때 실행
        // --------------
    }


    @Override
    public void onClick(View v) { // 클릭 이벤트 발생
        //추가 -------
        if (!first){ // 게임 시작
            iv_gun.setClickable(true);  //iv_gun 클릭 이벤트 활성화
            iv_gun.setOnClickListener(this); // iv_gun 뷰 클릭될 때 실행
            iv_start.setVisibility(View.INVISIBLE); // 뷰를 INVISIBLE로 설정
            first = true; // 버튼 눌러짐

            clay_translateX.start(); // clay_translateX 시작
            clay_translateY.start(); // clay_translateY 시작
            clay_rotation.start(); // clay_rotation 시작
            redclay_translateX.start(); // redclay_translateX 시작
            redclay_translateY.start(); // redclay_translateY 시작
            redclay_rotation.start(); // redclay_rotation 시작

        }
        else{
        if (remain_b == 0) // 만약 총알이 0개라면
        {
            iv_gun.setClickable(false); // iv_gun 클릭 이벤트 비활성화
            no_of_clays_left = 1; // 클레이 멈추기
            Toast.makeText(getApplicationContext(), "총알 소진 게임종료!!", Toast.LENGTH_LONG).show(); // 총알 소진

        }
        // ------------------------------
        else {
            remain_b--; // 총알 감소
            iv_bullet.setVisibility(View.VISIBLE); // bullet 뷰를 INVISIBLE -> VISIBLE

            ObjectAnimator bullet_scaleDownX = ObjectAnimator.ofFloat(iv_bullet, "scaleX", 1.0f, 0.0f); // bullet_scaleDownX 애니메이션 생성 - > 클릭시 크기 감소
            ObjectAnimator bullet_scaleDownY = ObjectAnimator.ofFloat(iv_bullet, "scaleY", 1.0f, 0.0f); // bullet_scaleDownY 애니메이션 생성 - >클릭시 크기 감소

            double bullet_x = gun_center_x - bullet_width / 2; // 총알의 x 값 위치는 중심부로 부터 너비의 1/2 값만큼 뺀 위치이다.
            ObjectAnimator bullet_translateX = ObjectAnimator.ofFloat(iv_bullet, "translationX", (float) bullet_x, (float) bullet_x);
            // bullet_translateX 애니메이션 생성
            ObjectAnimator bullet_translateY = ObjectAnimator.ofFloat(iv_bullet, "translationY", (float) gun_y, 30);
            // bullet_translateY 애니메이션 생성

            bullet_translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // bullet_translateY 애니메이션 과정에서 실행할 리스너 추가
                @Override
                public void onAnimationUpdate(ValueAnimator animation) { // 애니메이션 과정에서 실행
                    bullet_center_x = iv_bullet.getX() + bullet_width * 0.5f;
                    // bullet의 중심 x 값은 현재 bullet으로부터 받은 x값에 bullet 너비의 0.5f 만큼 더한 값
                    bullet_center_y = iv_bullet.getY() + bullet_height * 0.5f;
                    // bullet의 중심 y 값은 현재 bullet으로부터 받은 y값에 bullet 높이의 0.5f 만큼 더한 값

                    clay_center_x = iv_clay.getX() + clay_width * 0.5f;
                    // clay의 중심 x 값은 현재 clay으로부터 받은 x값에 clay 너비의 0.5f 만큼 더한 값
                    clay_center_y = iv_clay.getY() + clay_height * 0.5f;
                    // clay의 중심 y 값은 현재 clay으로부터 받은 y값에 clay 높이의 0.5f 만큼 더한 값

                    // 추가 -----------------
                    red_clay_center_x = iv_redclay.getX() + redclay_width * 0.5f;
                    // redclay의 중심 x 값은 현재 clay으로부터 받은 x값에 clay 너비의 0.5f 만큼 더한 값
                    red_clay_center_y = iv_clay.getY() + redclay_height * 0.5f;
                    // redclay의 중심 y 값은 현재 redclay으로부터 받은 y값에 clay 높이의 0.5f 만큼 더한 값
                    // -----------------------

                    double dist = Math.sqrt(Math.pow(bullet_center_x - clay_center_x, 2) + Math.pow(bullet_center_y - clay_center_y, 2)); //초록클레이와 총알의 거리 측정
                    double red_dist = Math.sqrt(Math.pow(bullet_center_x - red_clay_center_x, 2) + Math.pow(bullet_center_y - red_clay_center_y, 2)); //빨간클레이와 총알의 거리 측정
                    if (dist <= 100) { // 만약 총과 클레이의 거리가 100보다 작다면 // 수정 -> 200이 생각보다 범위가 넓음
                        hit = 1; // 추가
                        iv_clay.setVisibility(View.INVISIBLE); // clay INVISIBLE
                    }

                    // 추가 ---------------
                    if (red_dist <= 100) { // 만약 총과 클레이의 거리가 100보다 작다면 // 수정 -> 200이 생각보다 범위가 넓음
                        red_hit = 1; // 추가
                        iv_redclay.setVisibility(View.INVISIBLE); // redclay INVISIBLE
                    }
                    // ---------------------
                }
            });

            AnimatorSet bullet = new AnimatorSet(); // AnimatorSet bullet 객체 생성
            bullet.playTogether(bullet_translateX, bullet_translateY, bullet_scaleDownX, bullet_scaleDownY);
            // 동시에 bullet_translateX, bullet_translateY, bullet_scaleDownX, bullet_scaleDownY 애니메이션 세트 생성
            bullet.setDuration(500); // bullet 500동안 진행
            bullet.start(); // bullet 시작
            }
        }
    }
}

