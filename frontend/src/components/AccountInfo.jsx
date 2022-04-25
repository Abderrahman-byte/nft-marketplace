import React from "react";


import '../styles/AccountInfo.css'



const AccountInfo = (/*{ displayname, setdisplayname}*/{ /*items, setitems,*/ profile, setprofile}) => {

    return (

        <div className="accountInfo">
            <span className="Account-Info">
                Account Info 
            </span>
            <div className="Display-name">
                <label > Display Name</label>
                {/*<input onChange={e => setdisplayname(e.target.value)} name='search' placeholder=' Enter your display name' value={displayname} className='Display-name-input' autoComplete='off' />*/}
                {/*<input onChange={e => setitems({...items, displayName : e.target.value})} name='displayname' placeholder=' Enter your display name' value={items.displayname} className='Display-name-input' autoComplete='off' />*/}
                {/*<input onChange={e => setitems({...items, displayName : e.target.value})} name='search' placeholder=' Enter your display name' defaultValue={profile.displayName} className='Display-name-input' />*/}
                <input onChange={e => setprofile({...profile, displayName : e.target.value})} name='search' placeholder=' Enter your display name'
                 defaultValue=  {profile.displayName}
                 className='Display-name-input' />
            </div>
            <div className="CustomUrl">
                <label > Custom URL</label>     
                {/*<input name='search' placeholder=' Enter your display name' className='CustomUrl-input' autoComplete='off' />*/}
                {/*<input onChange={e=> setitems({ ...items, customUrl : e.target.value })}  name='customurl' placeholder=' Enter your display name' value={items.customUrl} className='CustomUrl-input' autoComplete='off' />*/}
                {/*<input onChange={e=> setitems({ ...items, customUrl : e.target.value })}  name='search' placeholder=' Enter your display name'  defaultValue={profile.customUrl} className='CustomUrl-input'  />*/}
                <input onChange={e=> setprofile({ ...profile, customUrl : e.target.value })}  name='search' placeholder=' Enter your display name'  defaultValue={profile.customUrl } className='CustomUrl-input'  />
            </div>
            <div className="Bio">
                <label >Bio</label>
                {/*<input name='search' placeholder='  About yourselt in a few words' className='Bio-input' autoComplete='off' />*/}
                {/*<input onChange={e=> setitems({ ...items, bio : e.target.value })} name='bio' placeholder='  About yourselt in a few words' value={items.bio} className='Bio-input' autoComplete='off' />*/}
               {/* <input onChange={e=> setitems({ ...items, bio : e.target.value })} name='search' placeholder='  About yourselt in a few words'  defaultValue={profile.bio} className='Bio-input' autoComplete='off' />*/}
               <input onChange={e=> setprofile({ ...profile, bio : e.target.value })} name='search' placeholder='  About yourselt in a few words'  defaultValue={profile.bio } className='Bio-input' autoComplete='off' />

            </div>
           
        </div>

    )



}

export default AccountInfo;