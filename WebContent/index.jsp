<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>テーブル選択画面</title>
    <link rel="stylesheet" href="css/style.css">
    <%-- 外部JSファイルを読み込む（ここがあれば下のスクリプトは不要です） --%>
<%--     <script src="${pageContext.request.contextPath}/js/table-control.js" defer></script> --%>
</head>

<body>
    <!-- <table id="data-table" border="1">
        <thead>
            <tr>
                <th>選択</th>
                <th>ID</th>
                <th>名前</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><input type="checkbox" class="row-checkbox"></td>
                <td>1</td>
                <td>データA</td>
            </tr>
            <tr>
                <td><input type="checkbox" class="row-checkbox"></td>
                <td>2</td>
                <td>データB</td>
            </tr>
            <tr>
                <td><input type="checkbox" class="row-checkbox"></td>
                <td>3</td>
                <td>データC</td>
            </tr>
        </tbody>
    </table> -->
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

    <%--
      もし外部ファイルを使わず、このファイルに直接書きたい場合は、
      以下のように <script> タグで囲みます。
    --%>
<!--     <script>
        document.addEventListener('DOMContentLoaded', () => {
            const checkboxes = document.querySelectorAll('.row-checkbox');
            const submitBtn = document.getElementById('submit-btn');

            checkboxes.forEach(checkbox => {
                checkbox.addEventListener('change', () => {
                    const checkedCount = document.querySelectorAll('.row-checkbox:checked').length;
                    submitBtn.disabled = (checkedCount !== 2); // スッキリ書くならこう！
                });
            });
        });
    </script> -->
    <script>
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