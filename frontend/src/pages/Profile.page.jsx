import React, {useEffect, useState}from "react";
import Profile from "../components/Profile";
import { getProfile } from "../utils/api";
import UpdateProfilepage from "./UpdateProfile.page";

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
   
   

    return (
        <div className="Profile">


            
           
                  <Profile profile ={profile}  />
                  

        </div>
    )




}

export default Profilepage;



