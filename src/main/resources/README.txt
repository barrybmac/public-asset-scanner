        AWS Asset Scanner - Scan ALL the Services to see if they are public facing.
        Copyright (C) 2016  Barry McCall AdTheorent LLC

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.

---------------------------------------------------------------------------------------------

DO NOT allow credentials files to get committed to GitHub!!! You WILL get pwned!
Please be sure your .gitignore omits this dir if you are hosting source publicly!

AWS Credentials format:
    example.awscredentials.properties
    account1.awscredentials.properties
    account2.awscredentials.properties


File contents:
    accessKey = AAAAABBBBBCCCCCDDDDD
    secretKey = V2VsbCwgeW91IHRyaWVkIHJpZ2h0Pw==