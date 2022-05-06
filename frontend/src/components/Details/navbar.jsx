import React from 'react'


import './styles.css'

const DetailNavbar = ({ setnext}) => {
    const links = [
        {
            text: 'Info',
            link: '1'
        }, {
            text: 'Owners',
            link: '2'
        }, {
            text: 'History',
            link: '3'
        }, {
            text: 'Bids',
            link: '4'
        }
    ]

    return (
        <nav className='DetailNavbar'>
            {links.map((link, i) => <span key={i} onClick={()=>{setnext(link.link)

            }} >{link.text}</span>)}
        </nav>
    )
}

export default DetailNavbar