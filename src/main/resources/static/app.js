// Add event listener to the search button
document.getElementById('searchButton').addEventListener('click', fetchUserInfo);

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
            document.querySelector('.user-info h2').innerText = `${userName}`;
        })
        .catch(error => console.error('Error fetching user ID:', error));

    // Fetch User Profile Name and display for all elements with the class 'user-name'
    fetch(`http://localhost:8080/api/twitter/userProfileName?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            // Update all elements with the class 'user-name'
            document.querySelectorAll('.user-name').forEach(element => {
                element.innerText = `${data}`;
            });
        })
        .catch(error => console.error('Error fetching user profile name:', error));

    // Update all elements with the class 'user-@name'
    fetch(`http://localhost:8080/api/twitter/userProfileName?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.querySelectorAll('.user-@name').forEach(element => {
                element.innerText = `@${data}`;
            });
        })
        .catch(error => console.error('Error fetching user profile name:', error));

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
        .catch(error => console.error('Error fetching following count:', error));

    // Fetch Tweet Count
    fetch(`http://localhost:8080/api/twitter/userTweetCount?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('posts-count').innerText = data;
        })
        .catch(error => console.error('Error fetching tweet count:', error));

    // Fetch Banner Image
    fetch(`http://localhost:8080/api/twitter/userBanner?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('banner-image').src = data;
        })
        .catch(error => console.error('Error fetching banner image:', error));

    // Fetch Profile Picture for all elements with the class 'profile-img'
    fetch(`http://localhost:8080/api/twitter/userProfilePicture?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.querySelectorAll('.profile-img').forEach(element => {
                element.src = data;
            });
        })
        .catch(error => console.error('Error fetching profile picture:', error));

    // Fetch tweet content
    fetch(`http://localhost:8080/api/twitter/userRecentTweets?userName=${userName}`)
        .then(response => response.text())
        .then(data => {
            document.querySelector('#tweet-content p').innerText = data;
        })
        .catch(error => console.error('Error fetching tweet content:', error));
}
