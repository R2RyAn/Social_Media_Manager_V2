function fetchUserInfo() {
    // Get the username entered by the user
    const userName = document.getElementById('twitterUsername').value.trim();

    if (!userName) {
        alert('Please enter a Twitter username');
        return;
    }

    // Clear previous results
    document.getElementById('followers-count').innerText = '';
    document.getElementById('following-count').innerText = '';
    document.getElementById('posts-count').innerText = '';

    // Fetch User ID and display (if needed)
    fetch(`http://localhost:8080/api/twitter/userId?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            // Update account name with user ID (optional)
            document.querySelector('.user-info h2').innerText = `${userName}`;
        })
        .catch(error => console.error('Error fetching user ID:', error));


    // Fetch User name and display (if needed)
    fetch(`http://localhost:8080/api/twitter/userProfileName?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            // Update profile name with user ID (optional)
            document.querySelector('.user-info p').innerText = `@${data}`;
        })
        .catch(error => console.error('Error fetching user ID:', error));


    // Fetch Follower Count
    fetch(`http://localhost:8080/api/twitter/userFollowerCount?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('followers-count').innerText = data;
        })
        .catch(error => console.error('Error fetching follower count:', error));


    // Fetch Following Count
    fetch(`http://localhost:8080/api/twitter/userFollowingCount?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('following-count').innerText = data;
        })
        .catch(error => console.error('Error fetching follower count:', error));

    // Fetch Tweet Count
    fetch(`http://localhost:8080/api/twitter/userTweetCount?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('posts-count').innerText = data;
        })
        .catch(error => console.error('Error fetching follower count:', error));

    //Fetch Banner
    fetch(`http://localhost:8080/api/twitter/userBanner?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('banner-image').src = data;

        })
        .catch(error => console.error('Error fetching follower count:', error));

    //Fetch Profile Picture
    fetch(`http://localhost:8080/api/twitter/userProfilePicture?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('profile-img').src = data;

        })
        .catch(error => console.error('Error fetching follower count:', error));
    // (Optional) Add API calls to fetch more social media data like following, posts, etc.
}
