// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions
//terminal 指令
//firebase deploy --only functions

//exports.helloWorld = functions.https.onRequest((request, response) => {
//response.send("Hello from Firebase!");
//});
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();


// 抓取要傳遞給此 HTTP 端點的 text 參數值並將這個值新增到 Realtime Database 下的路徑 
`/messages/:pushId/original`
//test function
/* exports.addMessage = functions.https.onRequest((req, res) => {
  const original = req.query.text; //抓取訊息內容
  // 使用 Firebase Admin SDK 將訊息新增到 Realtime Database
  return admin.database().ref('/messages').push({original: original}).then((snapshot) => {
    return res.redirect(303, snapshot.ref.toString());
  });
});
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
    .onCreate((snapshot, context) => {
      const original = snapshot.val();
      console.log('Uppercasing', context.params.pushId, original);
      const uppercase = original.toUpperCase();
      return snapshot.ref.parent.child('uppercase').set(uppercase);
    }); */
    
// operate data to "processeddata/temperature/day"
exports.processTemDay = functions.database.ref('/dataset/temperature/{pushId}/{pushIdRam}')
    .onCreate((snapshot, context) => {
        const original = snapshot.val();
        //20190707(13)
        const date = context.params.pushId.substring(0,8);//20190707
        const time = context.params.pushId.substring(8,10);//13
        const locate = original["locate"]; 
        const datapath = "processeddata/temperature/day/" + locate + '/'+ date +'/'+ time;
        var newdata = original["data"]; 
        console.log('processTemDay', context.params.pushId, original,datapath);
        console.log(original["data"]);
        
        //catch old data (processeddata/temperature/day/../../..) 
        admin.database().ref(datapath).once("value", targettime => {
            var isExist = targettime.val();//old data
            console.log(newdata);//new data
            if (isExist){
                console.log("time exists!"+ isExist);
                newdata = newdata*0.5 + isExist*0.5;//average
                admin.database().ref(datapath).set(newdata);
            }
            else{
                 console.log("not exists!");
                 admin.database().ref(datapath).set(newdata);        
            }
        });
        return;
    });
    // operate data to "processeddata/co2"
exports.processTemDay = functions.database.ref('/dataset/co2/{pushId}/{pushIdRam}')
    .onCreate((snapshot, context) => {
        const original = snapshot.val();
        const locate = original["locate"]; 
        const boxID = original["boxID"];
    
        var newdata = {
            value: original["data"],
            x: original["x"],
            y: original["y"]
        }
       
        const datapath = "processeddata/co2/" + locate + '/BOX' + boxID;
        console.log('processco2', context.params.pushId, original,datapath);
        console.log(original["data"]);
       
        return admin.database().ref(datapath).set(newdata);
    });
    
   
    
    
    
    
    