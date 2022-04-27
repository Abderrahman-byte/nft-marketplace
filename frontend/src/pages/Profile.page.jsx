import React, {useEffect, useState}from "react";
import Profile from "../components/Profile";
import { getProfile } from "../utils/api";
import UpdateProfilepage from "./UpdateProfile.page";
import { sendProfilepicture } from "../utils/api";


const Profilepage = () => {
    const [profile, setprofile]= useState({});
    const submitCallback = async () => {
       
       
        const [success, Profile] = await getProfile();
     
        console.log("1111---->"+success)
        console.log(Profile)
        setprofile(Profile)
        console.log("here object")
        console.log(Profile)
     }
     useEffect(()=>{

         console.log("this is use effect")
         submitCallback();
     },[])

     const pictureChanged =  async (e) => {
        // setimage(e.target.files[0])
       
        const [success, err] = await sendProfilepicture(e.target.files[0], 'cover');
        console.log(success)
 
        console.log(err)
        window.top.location = window.top.location
        /* const fileReader = new FileReader()
 
         fileReader.onload = e => setImageUrl(e.target.result)
 
         fileReader.readAsDataURL(e.target.files[0])*/
     }
    
   

    return (
        <div className="Profile">


            
           
                  <Profile profile ={profile} pictureChanged ={pictureChanged}  />
                  

        </div>
    )




}

export default Profilepage;



