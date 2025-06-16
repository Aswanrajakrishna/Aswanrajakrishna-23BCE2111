package simple;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.Scanner;

public class StudentApp {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("college");
        MongoCollection<Document> collection = database.getCollection("students");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Reg No");
            System.out.println("4. Update Email");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.println("7. Update/Add Marks");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Reg No: ");
                    String regno = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Department: ");
                    String dept = sc.nextLine();

                    System.out.println("Enter marks out of 100:");
                    System.out.print("Maths: ");
                    int maths = sc.nextInt();
                    System.out.print("Physics: ");
                    int physics = sc.nextInt();
                    System.out.print("Chemistry: ");
                    int chemistry = sc.nextInt();
                    sc.nextLine();

                    Document marks = new Document("Maths", maths)
                            .append("Physics", physics)
                            .append("Chemistry", chemistry);

                    Document student = new Document("Name", name)
                            .append("RegNo", regno)
                            .append("Email", email)
                            .append("Department", dept)
                            .append("Marks", marks);

                    collection.insertOne(student);
                    System.out.println("Student added.");
                    break;

                case 2:
                    System.out.println("\n--- All Students ---");
                    for (Document d : collection.find()) {
                        System.out.println(d.toJson());
                    }
                    break;

                case 3:
                    System.out.print("Enter Reg No to search: ");
                    String searchReg = sc.nextLine();
                    Document found = collection.find(Filters.eq("RegNo", searchReg)).first();
                    if (found != null) {
                        System.out.println("Student Found:\n" + found.toJson());
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Reg No to update email: ");
                    String updateReg = sc.nextLine();
                    System.out.print("Enter new email: ");
                    String newEmail = sc.nextLine();

                    Bson update = Updates.set("Email", newEmail);
                    collection.updateOne(Filters.eq("RegNo", updateReg), update);
                    System.out.println("Email updated.");
                    break;

                case 5:
                    System.out.print("Enter Reg No to delete: ");
                    String delReg = sc.nextLine();
                    collection.deleteOne(Filters.eq("RegNo", delReg));
                    System.out.println("Student deleted.");
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                case 7:
                    System.out.print("Enter Reg No to update/add marks: ");
                    String regNumForMarks = sc.nextLine();

                    Document existingStudent = collection.find(Filters.eq("RegNo", regNumForMarks)).first();

                    if (existingStudent != null) {
                        System.out.println("Enter new marks out of 100:");
                        System.out.print("Maths: ");
                        int newMaths = sc.nextInt();
                        System.out.print("Physics: ");
                        int newPhysics = sc.nextInt();
                        System.out.print("Chemistry: ");
                        int newChem = sc.nextInt();
                        sc.nextLine();

                        Document newMarks = new Document("Maths", newMaths)
                                .append("Physics", newPhysics)
                                .append("Chemistry", newChem);

                        Bson marksUpdate = Updates.set("Marks", newMarks);
                        collection.updateOne(Filters.eq("RegNo", regNumForMarks), marksUpdate);

                        System.out.println("Marks updated successfully.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        mongoClient.close();
        sc.close();
    }
}
