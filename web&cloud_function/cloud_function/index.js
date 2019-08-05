// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

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
exports.processTemDay = functions.database.ref('/dataset/temperature/{pushId}')
    .onCreate((snapshot, context) => {
      const original = snapshot.val();
      
      //determine if this is a new day 
        //catch processeddata/temperature/day 
        //20190707(1330)
        var date = context.params.pushId.substring(0,8);//20190707
        var time = context.params.pushId.substring(8,10);//13
        var datapath = "processeddata/temperature/day/" + date +'/'+ time;
        
        console.log('processTemDay', context.params.pushId, original,datapath);
        //console.log(original["data"]);
        var newdata = original["data"];
        var isExist = 0;
        //catch old data (wrong
        admin.database().ref(datapath).once("value", targettime => {
            isExist = targettime.val();
            if (targettime.before.exists()){
            console.log("time exists!"+ isExist);
            }
            console.log(targettime.before.exists(),targettime.after.exists(),targettime.before.val());
        });
        //calculate avg
      return admin.database().ref(datapath).set(newdata);
    });
exports.processTemMonth = functions.database.ref('/dataset/temperature/{pushId}')
    .onCreate((snapshot, context) => {
      const original = snapshot.val();
      
      //determine if this is a new day 
        //catch processeddata/temperature/day 
        //201907(07)(1330)
        var date = context.params.pushId.substring(0,6);//201907
        var time = context.params.pushId.substring(6,8);//07
        var datapath = "processeddata/temperature/month/" + date +'/'+ time;
        
        console.log('processTemMonth', context.params.pushId, original,datapath);
        //console.log(original["data"]);
        var newdata = original["data"];
        var isExist = 0;
        //catch old data (wrong
        admin.database().ref(datapath).once("value", targettime => {
            isExist = targettime.val();
            if (targettime.before.exists()){
            console.log("time exists!"+ isExist);
            }
            console.log(targettime.before.exists(),targettime.after.exists(),targettime.before.val());
        });
        //calculate avg
      return admin.database().ref(datapath).set(newdata);
    });
    
    
    
    
    
    
    
    
    