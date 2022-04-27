import React, {useState} from 'react'

import ProfilePhoto from './ProfilePhoto'
import ProfileForm from './ProfileForm'
import '../styles/ProfileSettings.css'

import { sendProfile, sendProfilepicture } from "../utils/api";

const ProfileSettings = ({profile, setprofile}) => {
	const [image, setimage]= useState("")
	const submit = async (e) => {
		e.preventDefault()
	   const data={
		  displayName : profile.displayName,
		  customUrl : profile.customUrl,
		  bio: profile.bio
		}
		console.log("from settings")
		console.log(data)
		/*sending image to db*/
		console.log(typeof image)
	     const [success1, err1] = await sendProfile(data);
			console.log(err1)
			console.log(success1)
		
		 const [success, err] = await sendProfilepicture(image, 'avatar');
	    console.log(err)
	    console.log(success)
	   
	  }
	return (
		<div className='ProfileSettings'>
			<ProfilePhoto image = {profile.avatarUrl} setimage={setimage}  />
			<ProfileForm profile = {profile}  setprofile ={setprofile} submit={submit}/>
		</div>
	)
}

export default ProfileSettings
