import React, {useEffect, useState} from "react";
import '../styles/ProfilePhoto.css'




const ProfilePhoto = ({image, setimage}) => {
    
    const [imageUrl, setImageUrl] = useState(image)
   
    const pictureChanged = (e) => {
        setimage(e.target.files[0])

        const fileReader = new FileReader()

        fileReader.onload = e => setImageUrl(e.target.result)

        fileReader.readAsDataURL(e.target.files[0])
    }

    return (
       
        <div className="ProfilePhoto">

            <div className="Avatar">
            
                 <img className="Pic" src= {imageUrl} />
            </div>
          
            <div className="Frame-944">
                <div className="Frame-945">
                    <span className="Profile">Photo de profile </span>
                    <span className="Recomendation"> We recommend an image
                        of at least 400x400. Gifs work too 🙌</span>

                </div>
                {/*<button className='btn btn-white' > Upload</button>*/}
               <div className=" btn btn-white">
                <label for ="img"> Upload </label>
                <input className='inputfile' type="file" id="img" name="img" accept="image/*" onChange={pictureChanged}  />
                </div>
            </div>

        </div>
    )


}

export default ProfilePhoto;