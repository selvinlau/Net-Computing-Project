$(function () {
    // if user is running mozilla then use it's built-in WebSocket
    window.WebSocket = window.WebSocket || window.MozWebSocket;

    var connection = new WebSocket('ws://127.0.0.1:5678');
	var array=[];
	var fliterMode = "online";

    connection.onopen = function () {
        // connection is opened and ready to use
		console.log("Requestion Connected Devices");
		connection.send("0");
    };

    connection.onerror = function (error) {
        // an error occurred when sending/receiving data
    };

    connection.onmessage = function (message) {
        // try to decode json (I assume that each message from server is json)
        try {
			
			var obj = JSON.parse(message.data);
				var handled = false;
				
				if(obj.modeType == 1){
					console.log("deletion");
					var num0 = parseInt($("#online").html());
					num0--;
					$("#online").html(num0);
					for(var i = 0;array.length>i;i++){
						if(obj.name == array[i].name){
							if($("#"+obj.name).hasClass("list-group-item-danger")){
								var num = parseInt($("#bad").html());
								num--;
								$("#bad").html(num);
							}else{
								var num1 = parseInt($("#good").html());
								num1--;
								$("#good").html(num1);
							}
							
							array.splice(i, 1);
							$("#"+obj.name).remove();
							console.log(array);
							break;
						}
					} 
					handled=true;
				}
				else if(Array.isArray(obj)){
					console.log("Initializing Machine");
					//initialize
					array = obj;
					var html="";
					for(var i = 0;array.length>i;i++){
						var num0 = parseInt($("#online").html());
						num0++;
						$("#online").html(num0);
						var additionalClass = "";
						if(array[i].temperature>30){
							console.log("HOT Temp:"+array[i].temperature);
							additionalClass = "list-group-item-danger bad";
							var num = parseInt($("#bad").html());
							num++;
							$("#bad").html(num);
						}else{
							additionalClass = "good";
							var num = parseInt($("#good").html());
							num++;
							$("#good").html(num);
						}
							
						html+="<a href=\"#\" id=\""+array[i].name+"\"class=\"list-group-item "+additionalClass+"\"><span class=\"badge\">"+array[i].temperature.toFixed(2);
						html+="°C</span><i class=\"fa fa-fw fa-calendar\"></i>"+array[i].name+"</a>";
						
						
						$("#machineList").append(html);
						html="";
						
					}
					
					handled = true;
					
				}else if(obj.length !=0){
					//update
					
					for(var i = 0;array.length>i;i++){
						if(obj.name == array[i].name){
							console.log("Update Detected");
							var html="";
							array[i].temperature = obj.temperature;
							var additionalClass = "";
							var state ="";
							if(obj.temperature>30){
								console.log("HOT Temp:"+array[i].temperature);
								additionalClass = "list-group-item-danger bad";
								state = "bad";
								if($("#"+array[i].name).hasClass("list-group-item-danger")){
									
								}else{
									var num = parseInt($("#good").html());
									num--;
									$("#good").html(num);
									var num1 = parseInt($("#bad").html());
									num1++;
									$("#bad").html(num1);
									
								}
							}else{
								additionalClass = "good";
								state = "good";
								if($("#"+array[i].name).hasClass("list-group-item-danger")){
									var num = parseInt($("#bad").html());
									num--;
									$("#bad").html(num);
									var num1 = parseInt($("#good").html());
									num1++;
									$("#good").html(num1);
									
								}
								
							}
							
							html+="<a href=\"#\" id=\""+array[i].name+"\"class=\"list-group-item "+additionalClass+"\"><span class=\"badge\">"+array[i].temperature.toFixed(2);
							html+="°C</span><i class=\"fa fa-fw fa-calendar\"></i>"+array[i].name+"</a>";
							$("#"+array[i].name).replaceWith(html);
							console.log("state:"+state+" mode:"+filterMode);
							if(state == filterMode || filterMode == "online"){
								$("#"+array[i].name).show();
							}else{
								$("#"+array[i].name).hide();
							}
							
							
							handled = true;
							break;
						}
					} 
					
				}
				if(!handled){
					console.log("Detected Connection. Creating HTML");
					//creating
					array.push(obj);
					var num0 = parseInt($("#online").html());
					num0++;
					$("#online").html(num0);
					var additionalClass = "";
					var state = ""
					if(obj.temperature>30){
						console.log("HOT Temp:"+obj.temperature);
						additionalClass = "list-group-item-danger bad";
						
						var num1 = parseInt($("#bad").html());
						num1++;
						$("#bad").html(num1);
						state="bad";
					}else{
						additionalClass = "good";
						var num1 = parseInt($("#good").html());
						num1++;
						$("#good").html(num1);
						state="good";
					}
							
					var html="<a href=\"#\" id=\""+obj.name+"\"class=\"list-group-item "+additionalClass+"\"><span class=\"badge\">"+obj.temperature.toFixed(2);
					html+="°C</span><i class=\"fa fa-fw fa-calendar\"></i>"+obj.name+"</a>";
					$("#machineList").append(html);
					if(state==filterMode || filterMode == "online"){
						$("#"+obj.name).show();
					}else{
						$("#"+obj.name).hide();
					}
					
				}
			json = array;
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message.data);
            return;
        }
        // handle incoming message
    };
	$( "#onlineFilter" ).click(function() {
		var badArray = document.getElementsByClassName("bad");
		filterMode = "online";
		for(var i = 0;badArray.length>i;i++){
			$("#"+badArray[i].id).show();
		}
		var goodArray = document.getElementsByClassName("good");
		for(var i = 0;goodArray.length>i;i++){
			$("#"+goodArray[i].id).show();
		}
	});
	$( "#goodFilter" ).click(function() {
		var badArray = document.getElementsByClassName("bad");
		filterMode = "good";
		for(var i = 0;badArray.length>i;i++){
			$("#"+badArray[i].id).hide();
		}
		var goodArray = document.getElementsByClassName("good");
		for(var i = 0;goodArray.length>i;i++){
			$("#"+goodArray[i].id).show();
		}
	});
	$( "#badFilter" ).click(function() {
		var badArray = document.getElementsByClassName("bad");
		filterMode = "bad";
		for(var i = 0;badArray.length>i;i++){
			$("#"+badArray[i].id).show();
		}
		var goodArray = document.getElementsByClassName("good");
		for(var i = 0;goodArray.length>i;i++){
			$("#"+goodArray[i].id).hide();
		}
	});
});