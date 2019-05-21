function checkAll(qx){
		var dxs=document.getElementsByName("dx");

		for(var i=0;i<dxs.length;i++){
			dxs[i].checked=qx.checked;
		}
	}

	function setqx(){
		var dxs = document.getElementsByName("dx");
		for(var i=0; i<dxs.length; i++){
			dxs[i].onclick=function(){
				var flag = true;
				var dxArr = document.getElementsByName("dx");
				for (var j=0; j<dxArr.length; j++) {
					if(!dxArr[j].checked){
						flag = false;
						break;
					}
				}
				if(flag){
					document.getElementById("quanxuan").checked=true;
				}else{
					document.getElementById("quanxuan").checked=false;
				}
			}
		}
	}