 function textAreaCounter(field,cntfield,maxlimit) 
			{
				
				if (field.value.length > maxlimit) // if too long...trim it!
					field.value = field.value.substring(0, maxlimit);
				// otherwise, update 'characters left' counter
				else
					cntfield.value = maxlimit - field.value.length;
			}
			
			
			
			
			
			 function specialCharectersCheck(e)
              {
                       var key;
                       var keychar;
                       if (window.event)
                                  key = window.event.keyCode;
                       else if (e)
                                  key = e.which;
                       else
                                  return true;
       
                       keychar = String.fromCharCode(key);
                       keychar = keychar.toLowerCase();
               // control keys
                       if ((key==0) || (key==8) || (key==9) || (key==13) || (key==27) || (key==127))
                                  return true;
       
               // alphas and numbers
                       else if ((("@#$%^&*'|\"~_").indexOf(keychar) > -1))
                                  return false;
                       else
                                  return true;
              }
	
	
	
	
	function intOnly(i) {
	
	if(i.value.length>0) {
	    	i.value=parseFloat(i.value); 
		i.value = i.value.replace(/[^\d]+/g, '');
	
	}
	
	}
	
	
	function comparefromTo(one,two,msgone,msgtwo,textbox){
    
   
   
   if(one.value=="" || two.value==""){
   return false;
   }
   var lhd=one.value.split("/");
   var ahd=two.value.split("/");
   

   var lastHDate = new Date(lhd[1]+"/"+lhd[0]+"/"+lhd[2])
   var adjToDate=new Date(ahd[1]+"/"+ahd[0]+"/"+ahd[2]);
   
  if(lastHDate >= adjToDate){
  alert("Please select "+msgtwo+" Date grater then the "+msgone+" Date");
  textbox.value="";
  textbox.focus();
  return false;
  } 
  
  
   
   }
	
	
	function comparefromToEqual(one,two,msgone,msgtwo,textbox){
    
   
   
   if(one.value=="" || two.value==""){
   return false;
   }
   var lhd=one.value.split("/");
   var ahd=two.value.split("/");
   

   var lastHDate = new Date(lhd[1]+"/"+lhd[0]+"/"+lhd[2])
   var adjToDate=new Date(ahd[1]+"/"+ahd[0]+"/"+ahd[2]);
   
  if(lastHDate > adjToDate){
  alert("Please select "+msgtwo+" Date grater then the "+msgone+" Date");
  textbox.value="";
  textbox.focus();
  return false;
  } 
  
  
   
   }
	
	 