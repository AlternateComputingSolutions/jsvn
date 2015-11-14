#!/bin/bash

SVNSERVER=svnrepo
SVNREPO="${1:-test}"
IMPORT_MESSAGE="create test repo"
FILE=file1
FILE_CONTENT="a single file"
SVN_URL="file://$PWD/$SVNSERVER"
SVN_URL_REPO="$SVN_URL/$SVNREPO"

svnadmin create "$SVNSERVER"
mkdir -p import/"$SVNREPO"/{branches,trunk,tags}
svn import import "$SVN_URL" -m "$IMPORT_MESSAGE"
rm -rf import
git svn clone --stdlayout --prefix origin/ "$SVN_URL_REPO" gitclone
cd gitclone
cat > "$FILE" <<EOF
$FILE_CONTENT
EOF
git add "$FILE"
git commit -m "first file"
git svn dcommit
mkdir -p dir1/dir2
touch dir1/a
touch dir1/dir2/b
git add .
git commit -m "nested dirs and files"
git svn dcommit
mkdir groupA
echo "foo bar" > groupA/hoge
git add .
git commit -m "more dirs and files"
git svn dcommit
git svn branch dev -m "create new dev branch"
git svn tag 1.0 -m "tag 1.0"


git log --graph --color --oneline --all
cd ..
svn checkout "$SVN_URL_REPO/trunk" svncheckout
#svn log "$SVN_URL_REPO/trunk"
cd svncheckout
svn log
