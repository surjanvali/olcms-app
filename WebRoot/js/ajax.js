function getXMLRequest()
{
	var httpXMLObj = false;
//	this.sendXMLRequest = sendXMLRequest
//	this.getResponse = getResponse
//	this.callback = false

	if (window.ActiveXObject)
	{ //for ie
	  // use the ActiveX control for IE5.x and IE6 
		try
		{ 
			httpXMLObj = new ActiveXObject("Msxml2.XMLHTTP");
		}catch (e)
		{ try 	{ 
			httpXMLObj = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e)
		{ }
		}
	}
	else if (window.XMLHttpRequest)
	{ // for mozilla and firefox 
  	  // If IE7, Mozilla, Safari, etc: Use native object 
		/*try
		{ 
			netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
		} catch (e)
		{ 
			alert("Permission UniversalBrowserRead denied.");
		}*/
		httpXMLObj = false;
		httpXMLObj = new XMLHttpRequest();
		if (httpXMLObj.overrideMimeType)
		{ 
			httpXMLObj.overrideMimeType('text/xml');
		}
	
	}
	if( !httpXMLObj )
		alert("Internet Explorer 5.5 and above OR Mozilla FireFox \n is required for proper functionality \n of the application");
	return httpXMLObj;
}

function getResponseData(httpXMLObj)
{
	var xmldoc = false
	if (httpXMLObj && httpXMLObj.readyState == 4)
	{ 
		if (httpXMLObj.status == 200)
		{ 
			var string = httpXMLObj.responseText;
			
			if (window.ActiveXObject)
			{ 
				xmldoc = httpXMLObj.responseXML;
			}
			else if (window.XMLHttpRequest)
			{ 
				/* On IE7 the condition 'window.XMLHttpRequest' is valid but the object DOMParser does not exists, 
				so the code will fail. */
				// so swapped the if condition to check the above first
				var parser = new DOMParser(); 
				var doc = parser.parseFromString(string, "text/xml");
				xmldoc = doc.documentElement;
			}
		}
		else
		{
//			alert("ERROR")
		}
	}
	return xmldoc
}
/*function getResponseText( httpXMLObj )
{
	if (httpXMLObj.readyState == 4)
	{ 
		if (httpXMLObj.status == 200)
		{ 
			return httpXMLObj.responseText;
		}
	}
	return false;
}
//
/*function XMLRequest()
{
	this.httpXMLObj = false;
	this.sendXMLRequest = sendXMLRequest
	this.getResponse = getResponse
	this.callback = false

	if (window.XMLHttpRequest)
	{ // for mozilla and firefox
		try
		{ 
			netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
		} catch (e)
		{ 
			alert("Permission UniversalBrowserRead denied.");
		}
		this.httpXMLObj = false;
		this.httpXMLObj = new XMLHttpRequest();
		if (this.httpXMLObj.overrideMimeType)
		{ 
			this.httpXMLObj.overrideMimeType('text/xml');
		}
	
	}else if (window.ActiveXObject)
	{ //for ie
		try
		{ 
			this.httpXMLObj = new ActiveXObject("Msxml2.XMLHTTP");
		}catch (e)
		{ try 	{ 
			this.httpXMLObj = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e)
		{ }
		}
	}
}

function sendXMLRequest(url, callback)
{
	if( this.httpXMLObj !== false )
	{
		this.httpXMLObj.onreadystatechange = getResponse;
		this.httpXMLObj.open('GET',url,true); // GET method
	  	this.httpXMLObj.send(null); // always use null for GET method
	}
}

function getResponse()
{
	if (this.httpXMLObj.readyState == 4)
	{ 
		if (this.httpXMLObj.status == 200)
		{ 
			var string = this.httpXMLObj.responseText;
			var xmldoc;
			if (window.XMLHttpRequest)
			{ 
				var parser = new DOMParser();
				var doc = parser.parseFromString(string, "text/xml");
				xmldoc = doc.documentElement;
			}
			else if (window.ActiveXObject)
			{ 
				xmldoc = this.httpXMLObj.responseXML;
			}
		}
	}
}*/