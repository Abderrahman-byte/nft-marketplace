import React from "react";
import '../styles/EditProfile.css'
import ProfileSettings from "./ProfileSettings";



const EditProfile = ({profile, setprofile}) => {


    return (
        <div className="Edit-profile">

            <div className="Headline">
                <span className="Edit-span"> Edit profile  </span>
                <span className="Headline-span">You can set preferred display name, create
                    <span className="text-style-1"> your profile URL </span>
                    and manage other personal settings.
                </span>
            </div>
            <ProfileSettings profile={profile} setprofile={setprofile} />

        </div>
    )


}
export default EditProfile;