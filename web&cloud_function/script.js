  // Your web app's Firebase configuration
  var firebaseConfig = {
   apiKey: "AIzaSyA09_y8gMt8z03DNhtXK7Z_jEr1XbKtQfc",
  authDomain: "sensingbox-2a962.firebaseapp.com",
  databaseURL: "https://sensingbox-2a962.firebaseio.com",
  projectId: "sensingbox-2a962",
  storageBucket: "sensingbox-2a962.appspot.com",
  messagingSenderId: "851158753970",
  appId: "1:851158753970:web:ea17d5efd0ac996f"

  };
  console.log(firebase);
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  var isLogin = true;
  firebase.auth().signInWithEmailAndPassword('icedrip7@gmail.com', '123456')
  .catch(function(error) {
        if (error) {
            isLogin = false;
        }
    });
  console.log(isLogin);
  console.log(firebase);
 

  const db = firebase.database();
  console.log(db);
  var homebtn = document.querySelector("#home");
  var homebtn1 = document.querySelector("#home1");
  var homebtn2 = document.querySelector("#home2");
  var homebtn_box = document.querySelector("#home_box");
  var tmpbtn = document.querySelectorAll("#TEM");
  var co2btn = document.querySelectorAll("#CO2");
  var imagebtn = document.querySelectorAll("#IMG");
  var boxbtn = document.querySelector("#boxbtn");
  
  var main_page = document.querySelector("#main-page");
  var tmp_page = document.querySelector("#tem-page");
  var co2_page = document.querySelector("#co2-page");
  var image_page = document.querySelector("#image-page");
  var box_page = document.querySelector("#box-page");
  
  tmpbtn[0].addEventListener('click',showtmp);
  tmpbtn[1].addEventListener('click',showtmp);
  tmpbtn[2].addEventListener('click',showtmp);
  tmpbtn[3].addEventListener('click',showtmp);
  co2btn[0].addEventListener('click',showco2);
  co2btn[1].addEventListener('click',showco2);
  co2btn[2].addEventListener('click',showco2);
  co2btn[3].addEventListener('click',showco2);
  imagebtn[0].addEventListener('click',showimage);
  imagebtn[1].addEventListener('click',showimage);
  imagebtn[2].addEventListener('click',showimage);
  imagebtn[3].addEventListener('click',showimage);
  homebtn.addEventListener('click',showhome);
  homebtn1.addEventListener('click',showhome);
  homebtn2.addEventListener('click',showhome);
  homebtn_box.addEventListener('click',showhome);
  boxbtn.addEventListener('click',showbox);
   function showbox(){
      box_page.classList.remove('inactive'); 
      main_page.classList.add('inactive'); 
  }
  function showhome(){
      tmp_page.classList.add('inactive');
      co2_page.classList.add('inactive');
      image_page.classList.add('inactive'); 
      box_page.classList.add('inactive'); 
      main_page.classList.remove('inactive'); 
  }
  function showtmp(){
      tmp_page.classList.remove('inactive');
      co2_page.classList.add('inactive');
      image_page.classList.add('inactive'); 
      main_page.classList.add('inactive'); 
  }
  function showco2(){
      tmp_page.classList.add('inactive');
      co2_page.classList.remove('inactive');
      image_page.classList.add('inactive');    
      main_page.classList.add('inactive'); 
  }
  function showimage(){
      tmp_page.classList.add('inactive');
      co2_page.classList.add('inactive');
      image_page.classList.remove('inactive'); 
      main_page.classList.add('inactive');       
  }
