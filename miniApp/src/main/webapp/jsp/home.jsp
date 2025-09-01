<!-- ログイン後のホーム画面。 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.time.LocalDate, java.util.Map, java.util.HashMap, java.util.List, java.util.ArrayList, java.util.Collections, java.util.Arrays" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ホーム画面</title>
<link rel="stylesheet" type="text/css" href ="/miniApp/css/style.css"/>
<style>
    /* 占い結果のリストデザイン調整 */
    .post-block ul {
        list-style-type: none;   /* ←黒丸やダッシュを消す */
        padding-left: 0;         /* 左の余白も削除 */
        margin: 0;              /* 不要な余白も削除 */
    }

    .post-block li {
        margin-bottom: 5px;     /* 各項目の間隔を少し空ける */
        font-size: 1.05em;
    }
</style>
</head>

	<body>
		<h1>ホーム画面</h1>
		<p style ="text-align: right">${user.displayName}     様ログイン中</p>
		<p style ="text-align: right">
			<a href="/miniApp/logout">ログアウト</a>
		</p>
		
		<hr>
		
		<h1 class="board-title">掲示板</h1>
			<p style="text-align:center;">
    			<a href="<%= request.getContextPath() %>/board" class="highlight-button">掲示板を見る</a>
			</p>

<h2>★今日の星座占い★</h2>

<form method="post" style="margin-bottom: 10px;">
    <label for="zodiac">あなたの星座を選んでください：</label>
    <select name="zodiac" id="zodiac">
        <option value="おひつじ座">おひつじ座 (3/21 - 4/19)</option>
<option value="おうし座">おうし座 (4/20 - 5/20)</option>
<option value="ふたご座">ふたご座 (5/21 - 6/21)</option>
<option value="かに座">かに座 (6/22 - 7/22)</option>
<option value="しし座">しし座 (7/23 - 8/22)</option>
<option value="おとめ座">おとめ座 (8/23 - 9/22)</option>
<option value="てんびん座">てんびん座 (9/23 - 10/23)</option>
<option value="さそり座">さそり座 (10/24 - 11/22)</option>
<option value="いて座">いて座 (11/23 - 12/21)</option>
<option value="やぎ座">やぎ座 (12/22 - 1/19)</option>
<option value="みずがめ座">みずがめ座 (1/20 - 2/18)</option>
<option value="うお座">うお座 (2/19 - 3/20)</option>

    </select>
    <button type="submit">占う</button>
</form>

<%
request.setCharacterEncoding("UTF-8");
String zodiac = request.getParameter("zodiac");
if (zodiac != null && !zodiac.isEmpty()) {

    LocalDate today = LocalDate.now();
    int daySeed = today.getDayOfMonth() + today.getMonthValue() + today.getYear();

    int zodiacSeed = zodiac.hashCode();
    
 	// 星座リスト
    String[] zodiacList = {
        "おひつじ座","おうし座","ふたご座","かに座","しし座",
        "おとめ座","てんびん座","さそり座","いて座","やぎ座",
        "みずがめ座","うお座"
    };

    // 1～12の順位リストを作成して今日固定でシャッフル
    List<Integer> ranks = new ArrayList<>();
    for (int i = 1; i <= 12; i++) ranks.add(i);
    long seed = daySeed;
    Collections.shuffle(ranks, new java.util.Random(seed));

    // 選んだ星座のインデックス
    int zodiacIndex = Arrays.asList(zodiacList).indexOf(zodiac);

    // 今日の順位（重複なし）
    int rank = ranks.get(zodiacIndex);


    // ラッキーカラー候補
    String[] colors = {"レッド","グリーン","イエロー","ブルー","オレンジ","パープル","ピンク","ブラック","ゴールド","シルバー","ブラウン","ホワイト"};

    // 星座ごとのランダムカラー（今日だけ固定）
    int colorIndex = Math.abs((daySeed + zodiacSeed) % colors.length);
    String luckyColor = colors[colorIndex];
    
 	// 色名 → CSSカラーコード のマッピング
    Map<String, String> colorCssMap = new HashMap<>();
    colorCssMap.put("レッド", "red");
    colorCssMap.put("グリーン", "green");
    colorCssMap.put("イエロー", "gold");
    colorCssMap.put("ブルー", "blue");
    colorCssMap.put("オレンジ", "orange");
    colorCssMap.put("パープル", "purple");
    colorCssMap.put("ピンク", "deeppink");
    colorCssMap.put("ブラック", "black");
    colorCssMap.put("ゴールド", "gold");
    colorCssMap.put("シルバー", "silver");
    colorCssMap.put("ブラウン", "brown"); // ← レインボーの代わり
    colorCssMap.put("ホワイト", "gray");  // 白文字は見づらいので灰色に
    String luckyColorCss = colorCssMap.get(luckyColor);
    
 	// コメント候補
    String[] comments = {
    		"「幸せは、活動の中にある。」 — アリストテレス",
    		"「小さなことを大きな愛で行いなさい。」 — マザー・テレサ",
    	    "「どれほどゆっくり進もうとも、止まりさえしなければ問題ではない。」 — 孔子",
    	    "「幸せは準備が整った心に訪れる。」 — ダライ・ラマ",
    	    "「行動は言葉よりも力を持つ。」 — ニーチェ",
    	    "「過去に縛られず、今を生きよ。」 — 孔子",
    	    "「幸せは、見つけるものではなく作るもの。」 — アリストテレス",
    	    "「答えは外にではなく、心の中にある。」 — デカルト",
    	    "「一歩踏み出す勇気が、世界を変える。」 — カント",
    	    "「心の平穏は、思考の整理から。」 — スピノザ",
    	    "「人生は自分で描くものだ。」 — エマーソン",
    	    "「簡素に生きよ、そうすれば幸福も近くなる。」 — ソロー"
    		};

    // 星座ごとのランダムコメント（今日だけ固定）
    int commentsIndex = Math.abs((daySeed + zodiacSeed) % comments.length);
    String luckyComments = comments[commentsIndex];
%>

<div class="post-block">
    <p><strong><%= zodiac %></strong> の今日の運勢</p>
    <ul>
        <li>順位: <strong><span class="rank-number"><%= rank %></span>位</strong></li>
        <li>
    		ラッキーカラー:
    		<strong>
        		<span style="color:<%= luckyColorCss %>" class="lucky-color">
            		<%= luckyColor %>
        		</span>
    		</strong>
		</li>
        <li>一言コメント: <%= luckyComments %></li>
    </ul>
</div>

<% } %>

		
		
		
		
		
			
			<hr>			
			<p><a href="<%= request.getContextPath() %>/edit" class="button-link">メンバーの情報の編集</a></p><br> 
			<p><a href="<%= request.getContextPath() %>/delete" class="button-link">退会する方はこちら</a></p>
			
			
				
	</body>
</html>