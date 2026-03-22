<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.DataLog, java.util.List" %>
<%
    // サーブレット（Controller）からリクエストスコープで渡されたリストを取得
    List<DataLog> dataLogList = (List<DataLog>) request.getAttribute("dataLogList");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>テーブル選択画面</title>
<link rel="stylesheet" href="css/style.css">
</head>

<body>

	<h2>DBデータ一覧</h2>
    <p>比較したいデータを2つ選択してください。</p>
	<%= dataLogList %>
    <table id="data-table" border="1">
        <thead>
            <tr>
                <th>登録日時</th>
                </tr>
        </thead>
        <tbody>
            <%
            if (dataLogList != null && !dataLogList.isEmpty()) {
                for (DataLog dataLog : dataLogList) {
            %>
                <tr>
                    <td><%= dataLog.getWrDate() %></td>
                </tr>
            <%
                }
            } else {
            %>
                <tr>
                    <td>データが存在しません</td>
                </tr>
            <% } %>
        </tbody>
    </table>

	<button id="submit-btn" disabled style="margin-top: 20px;">送信する</button>


	<h2>ファイルのデータを表示する</h2>
	<input type="file" id="file-input" accept=".txt,.csv,.json">
	<hr>
	<h3>ファイルの内容</h3>
	<pre id="display-area">ファイルを選択してください</pre>

	<script>
		// HTML内の特定の部品（IDがついたもの）をJavaScriptから操作できるように変数に格納しています。
		const fileInput = document.getElementById('file-input');
		const displayArea = document.getElementById('display-area');

		// ファイルが新しく選択されたり、別のファイルに変更されたりした瞬間に、中の処理（{ } で囲まれた部分）が実行されます。
		fileInput.addEventListener('change', (event) => {
			// 選択されたファイルのリスト
			const file = event.target.files[0];
			if(!file) return;
			// 「ファイルの中身を読み取る専用の道具」を新しく用意する命令
			const reader = new FileReader();
			// 「読み込みが終わったら、この関数を実行してね」という予約
			reader.onload = (e) => {
				// 実際に読み取られたテキストデータが入る。
				const content = e.target.result;
				// 画面上に文字を表示
				displayArea.textContent = content;
			};

			reader.onerror = () => {
				alter('ファイルの読み込みに失敗しました');
			};
			// readAsText は、ファイルを「テキストデータ」として読み込むよう指示
			reader.readAsText(file);

		});

		document.addEventListener('DOMContentLoaded', () => {
		    const tableBody = document.querySelector('#data-table tbody');
		    const submitBtn = document.getElementById('submit-btn');

		    tableBody.addEventListener('click', (event) => {
		        // クリックされた要素の親にある tr を探す
		        const row = event.target.closest('tr');

		        // 「データが存在しません」の行やヘッダーなどは除外
		        if (!row || row.innerText.includes("データが存在しません")) return;

		        row.classList.toggle('selected');
		        const selectedCount = document.querySelectorAll('#data-table tbody tr.selected').length;
		        submitBtn.disabled = (selectedCount !== 2);
		    });
		});
    </script>
</body>
</html>