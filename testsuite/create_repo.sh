#!/bin/bash

SVNSERVER=svnrepo
SVNREPO=test
IMPORT_MESSAGE="create test repo"
FILE=file1
FILE_CONTENT="a single file"
SVN_URL="file://$PWD/$SVNSERVER"
SVN_URL_REPO="$SVN_URL/$SVNREPO"

svnadmin create "$SVNSERVER"
mkdir -p import/$SVNREPO/{branches,trunk,tags}
svn import import "$SVN_URL" -m "$IMPORT_MESSAGE"
rm -rf import
git svn clone --stdlayout "$SVN_URL_REPO" gitclone
cd gitclone
cat > "$FILE" <<EOF
$FILE_CONTENT
EOF
git add "$FILE"
git commit -m "first file"
git svn dcommit

git logga
cd ..
svn checkout "$SVN_URL_REPO/trunk" svncheckout
#svn log "$SVN_URL_REPO/trunk"
cd svncheckout
svn log
