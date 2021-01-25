

import React, { Component } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  TouchableOpacity,
  NativeModules,
  PermissionsAndroid
} from 'react-native';

const share = NativeModules.Share;

class App extends Component {

  share= async()=>{
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
      {
        title: " App Storage Permission",
        message:"",
        buttonNeutral: "Ask Me Later",
        buttonNegative: "Cancel",
        buttonPositive: "OK"
      }
    );
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        
        share.sharePngWhatsApp('test1',(error,res)=>{
          if(error){
           console.log(`Error found! ${error}`);
          }else{
            console.log(res);
         }
        })
   
      } else {
     
    }
}

  render(){
    return (
          <View style={styles.container}>
            <TouchableOpacity onPress={()=> this.share() } style={styles.button}>
              <Text style={{color:'white'}}>Click</Text>
            </TouchableOpacity>
          </View>
      );
  }
 
};

const styles = StyleSheet.create({
  container:{
    flex:1,
    alignItems:'center',
    justifyContent:'center'
  },
  button:{
    alignItems:'center',
    justifyContent:'center',
    paddingHorizontal:15,
    paddingVertical:10,
    backgroundColor:'blue',
    color:'white',
    borderRadius:10
  }
});

export default App;
