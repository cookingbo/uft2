<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>テーブル選択画面</title>
    <link rel="stylesheet" href="css/style.css">
<!--     <style>
    	body { font-family: sans-serif; margin: 20px; line-height: 106;}
    	#display-area {
    		background: #f4f4f4;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            white-space: pre-wrap; /* 改行や空白を保持 */
            min-height: 100px;
    	}

    </style> -->
</head>

<body>

	<table id="data-table" border="1">
		<thead>
			<tr>
				<th>ID</th>
				<th>名前</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>1</td>
				<td>データA</td>
			</tr>
			<tr>
				<td>2</td>
				<td>データB</td>
			</tr>
			<tr>
				<td>3</td>
				<td>データC</td>
			</tr>
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
        const rows = document.querySelectorAll('#data-table tbody tr');
        const submitBtn = document.getElementById('submit-btn');

        rows.forEach(row => {
            row.addEventListener('click', () => {
                // クリックされた行に 'selected' クラスがあれば消し、なければ付ける（トグル）
                row.classList.toggle('selected');

                // 現在 'selected' クラスを持っている行の数をカウント
                const selectedCount = document.querySelectorAll('#data-table tbody tr.selected').length;

                // 2つ選択されている時だけボタンを活性化
                submitBtn.disabled = (selectedCount !== 2);
            });
        });
    });
    </script>
</body>
</html>