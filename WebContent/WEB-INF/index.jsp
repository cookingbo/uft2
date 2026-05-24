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
<style>
	/* --- テーブル選択用の簡易スタイル --- */
	#data-table tbody tr { cursor: pointer; }
	#data-table tbody tr.selected { background-color: #d1ecf1; }

	/* --- ファイルアップロード用のスタイル --- */
	/* 本物のinputを見えなくするクラス */
	.visual-hidden {
		position: absolute;
		width: 1px;
		height: 1px;
		padding: 0;
		margin: -1px;
		overflow: hidden;
		clip: rect(0, 0, 0, 0);
		border: 0;
	}
	/* ラベル（自作ボタン）の装飾 */
	.custom-btn {
		display: inline-block;
		background: #333;
		color: #fff;
		padding: 6px 12px;
		border-radius: 4px;
		cursor: pointer;
		user-select: none;
	}
	.custom-btn:hover {
		background: #555;
	}
</style>
</head>

<div id="loading-overlay" class="overlay-hidden">
    <div class="loading-box">
        <div class="spinner"></div>
        <p>処理中です。少々お待ちください...</p>
    </div>
</div>

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

		<button type="button" id="submit-btn" disabled style="margin-top: 20px;">比較出力</button>
	</form>

	<!-- ファイルアップロード用フォーム（ボタンのみ） -->
	<form id="upload-form"	action="FileRegister" method="post" enctype="multipart/form-data">
		<input type="hidden" name="actionType" value="upload">

		<!-- 本物のfileインプットは隠したままにします -->
		<input type="file" name="uploadFile" id="uploadFile" class="visual-hidden">

		<!-- 修正箇所：ボタンを標準のbutton要素にし、クリック時にfileインプットを呼び出すようにします -->
		<button type="button" onclick="document.getElementById('uploadFile').click();">ファイル登録</button>
	</form>

	<script>
	    // 日時選択
		document.addEventListener('DOMContentLoaded', () => {
		    const tableBody = document.querySelector('#data-table tbody');
		    const submitBtn = document.getElementById('submit-btn');
		    const compareForm = document.getElementById('compare-form');
		    const loadingOverlay = document.getElementById('loading-overlay');

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

		    /* ========================================================
			 * 1. 比較出力（日時一覧を表示する処理）
			 * ======================================================== */
			submitBtn.addEventListener('click', () => {
				const selectedRows = document.querySelectorAll('#data-table tbody tr.selected');
				document.getElementById('hidden-date1').value = selectedRows[0].innerText.trim();
				document.getElementById('hidden-date2').value = selectedRows[1].innerText.trim();

				// 【修正】ダイアログを表示し、コンソールにログを出力
				console.log("[JSログ] 比較出力ボタンが押されました。ダイアログを表示します。");
				loadingOverlay.classList.remove('overlay-hidden');

				// 【安心の処理】少しだけ（50ミリ秒）待ってから送信することで、ブラウザにダイアログを描画させます
				setTimeout(() => {
					console.log("[JSログ] 比較フォームを送信します。");
					compareForm.submit();
				}, 50);
			});

			/* ========================================================
			 * 2. ファイル登録処理（アップロード）
			 * ======================================================== */
			const fileInput = document.getElementById('uploadFile');
			const uploadForm = document.getElementById('upload-form');

			fileInput.addEventListener('change', () => {
				if (fileInput.files.length > 0) {
					// 【修正】ダイアログを表示し、コンソールにログを出力
					console.log("[JSログ] ファイルが選択されました。ダイアログを表示します。選択ファイル: " + fileInput.files[0].name);
					loadingOverlay.classList.remove('overlay-hidden');

					// 【安心の処理】少しだけ（50ミリ秒）待ってから送信することで、ブラウザにダイアログを描画させます
					setTimeout(() => {
						console.log("[JSログ] ファイルアップロードフォームを送信します。");
						uploadForm.submit();
					}, 50);
				}
			});
		});
    </script>
</body>
</html>