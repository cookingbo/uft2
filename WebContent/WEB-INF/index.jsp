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
	<form id="compare-form" action="/uft2/Main" method="post">
		<input type="hidden" name="date1" id="hidden-date1">
		<input type="hidden" name="date2" id="hidden-date2">

		<h2>DBデータ一覧</h2>
	    <p>比較したいデータを2つ選択してください。</p>
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

		<button type="button" id="submit-btn" disabled style="margin-top: 20px;">送信する</button>
	</form>

	<h2>ファイルのデータを表示する</h2>
	<input type="file" id="file-input" accept=".txt,.csv,.json">
	<hr>
	<h3>ファイルの内容</h3>
	<pre id="display-area">ファイルを選択してください</pre>

	<footer></footer>

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
		    const compareForm = document.getElementById('compare-form');

		    tableBody.addEventListener('click', (event) => {
		        // クリックされた要素の親にある tr を探す
		        const row = event.target.closest('tr');

		        // 「データが存在しません」の行やヘッダーなどは除外
		        if (!row || row.innerText.includes("データが存在しません")) return;

		        row.classList.toggle('selected');
		        const selectedRows = document.querySelectorAll('#data-table tbody tr.selected');
		        const selectedCount = selectedRows.length;
		        submitBtn.disabled = (selectedCount !== 2);
		    });

		 	// ボタンクリック時の送信処理
		    submitBtn.addEventListener('click', () => {
		    	const selectedRows = document.querySelectorAll('#data-table tbody tr.selected');

		    	// 選択された2つの行から日時テキストを取得してhiddenにセット
		    	document.getElementById('hidden-date1').value = selectedRows[0].innerText.trim();
		    	document.getElementById('hidden-date2').value = selectedRows[1].innerText.trim();

		    	// フォームを送信
		    	compareForm.submit();
		    })
		});
    </script>
</body>
</html>